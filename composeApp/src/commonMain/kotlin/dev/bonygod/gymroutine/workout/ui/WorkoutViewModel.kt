package dev.bonygod.gymroutine.workout.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bonygod.gymroutine.auth.domain.usecase.GetCurrentUserUseCase
import dev.bonygod.gymroutine.core.navigation.BottomTab
import dev.bonygod.gymroutine.core.navigation.Navigator
import dev.bonygod.gymroutine.routines.domain.model.Routine
import dev.bonygod.gymroutine.routines.domain.usecase.GetRoutinesUseCase
import dev.bonygod.gymroutine.routines.domain.usecase.UpdateRoutineUseCase
import dev.bonygod.gymroutine.workout.domain.usecase.LogWorkoutUseCase
import dev.bonygod.gymroutine.workout.ui.interactions.WorkoutEffect
import dev.bonygod.gymroutine.workout.ui.interactions.WorkoutEvent
import dev.bonygod.gymroutine.workout.ui.interactions.WorkoutState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class WorkoutViewModel(
    private val navigator: Navigator,
    private val getCurrentUser: GetCurrentUserUseCase,
    private val logWorkout: LogWorkoutUseCase,
    private val getRoutines: GetRoutinesUseCase,
    private val updateRoutine: UpdateRoutineUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(WorkoutState())
    val state: StateFlow<WorkoutState> = _state

    private val _effect = MutableSharedFlow<WorkoutEffect>(replay = 1)
    val effect: SharedFlow<WorkoutEffect> = _effect.asSharedFlow()

    private var userId: String = ""
    private var currentRoutine: Routine? = null

    /**
     * Per-exercise debounce jobs. Keyed by exercise index.
     * A new edit within 300 ms cancels and reschedules the pending save for that exercise,
     * avoiding a Firestore write per keystroke while still persisting within ~300 ms of blur/Done.
     */
    private val saveJobs = mutableMapOf<Int, Job>()

    init {
        viewModelScope.launch {
            getCurrentUser().onSuccess { user ->
                userId = user?.uid.orEmpty()
            }
        }
    }

    fun onEvent(event: WorkoutEvent) {
        when (event) {
            is WorkoutEvent.OnInit -> loadExercises(event.routineId)
            is WorkoutEvent.OnToggleExercise -> {
                val state = _state.value
                if (event.index !in state.completedExercises && event.index !in state.skippedExercises) {
                    setState { toggleExpanded(event.index) }
                }
            }
            is WorkoutEvent.OnUpdateWeight -> setState { updateWeight(event.index, event.weight) }
            is WorkoutEvent.OnUpdateReps -> setState { updateReps(event.index, event.reps) }
            is WorkoutEvent.OnCompleteExercise -> setState { completeExercise(event.index) }
            is WorkoutEvent.OnToggleSkipExercise -> setState { toggleSkipExercise(event.index) }
            is WorkoutEvent.OnSaveExerciseProgress -> scheduleSave(event.index)
            is WorkoutEvent.OnFinishWorkout -> finishWorkout(event.routineId, event.routineName)
            is WorkoutEvent.OnBackClick -> onBackClick()
        }
    }

    private fun onBackClick() {
        // Flush any pending debounced saves synchronously before navigating away
        // so edits are never lost on back-press.
        viewModelScope.launch {
            cancelAndFlushSaveJobs()
            saveExerciseProgress()
            navigator.goBack()
        }
    }

    // ── Autosave ──────────────────────────────────────────────────────────────

    /**
     * Schedules a debounced save for [index]. If the same exercise already has a pending job
     * it is cancelled first — only one write fires per 300 ms window per exercise.
     */
    private fun scheduleSave(index: Int) {
        saveJobs[index]?.cancel()
        saveJobs[index] = viewModelScope.launch {
            delay(300L)
            saveExerciseProgress()
            saveJobs.remove(index)
        }
    }

    /** Cancels all pending debounced saves. Call before an immediate flush write. */
    private fun cancelAndFlushSaveJobs() {
        saveJobs.values.forEach { it.cancel() }
        saveJobs.clear()
    }

    // ── Load ──────────────────────────────────────────────────────────────────

    private fun loadExercises(routineId: String) {
        if (routineId.isBlank()) return
        viewModelScope.launch {
            getCurrentUser().onSuccess { user ->
                val uid = user?.uid.orEmpty()
                if (uid.isEmpty()) return@onSuccess
                getRoutines(uid).onSuccess { routines ->
                    routines.find { it.id == routineId }?.let { routine ->
                        currentRoutine = routine
                        setState { setExercises(routine.exercises) }
                    }
                }
            }
        }
    }

    // ── Finish ────────────────────────────────────────────────────────────────

    private fun finishWorkout(routineId: String, routineName: String) {
        if (userId.isEmpty()) return
        viewModelScope.launch {
            setState { showLogging() }
            cancelAndFlushSaveJobs()
            saveExerciseProgress()
            runCatching { logWorkout(userId, routineId, routineName, completado = true) }
                .onFailure { e -> setEffect(WorkoutEffect.ShowError(e.message.orEmpty())) }
            navigator.currentTab.value = BottomTab.Home
            navigator.goBack()
        }
    }

    // ── Persistence ───────────────────────────────────────────────────────────

    /**
     * Persists updated weight/reps to Firestore.
     *
     * Rules:
     * - Skipped exercises are excluded — their stored progression is never overwritten.
     * - Decimal separator is normalised (`,` → `.`) before parsing, handling ES/CA locales.
     * - Only writes when at least one value actually changed vs the loaded routine.
     * - Errors are surfaced as [WorkoutEffect.ShowError] instead of being silently swallowed.
     */
    private suspend fun saveExerciseProgress() {
        val routine = currentRoutine ?: return
        if (userId.isEmpty()) return
        val currentState = _state.value
        val updatedExercises = currentState.exercises.mapIndexed { i, ex ->
            if (i in currentState.skippedExercises) return@mapIndexed ex
            val form = currentState.exerciseForms[i]
            val newWeight = form?.weight?.replace(',', '.')?.toFloatOrNull() ?: ex.weight
            val newReps = form?.reps?.toIntOrNull() ?: ex.reps
            ex.copy(weight = newWeight, reps = newReps)
        }
        if (updatedExercises != currentState.exercises) {
            updateRoutine(userId, routine.copy(exercises = updatedExercises))
                .onFailure { e -> setEffect(WorkoutEffect.ShowError(e.message.orEmpty())) }
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private fun setState(reducer: WorkoutState.() -> WorkoutState) {
        _state.value = _state.value.reducer()
    }

    private suspend fun setEffect(effect: WorkoutEffect) {
        _effect.emit(effect)
    }
}
