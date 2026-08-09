package dev.bonygod.gymroutine.workout.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bonygod.gymroutine.auth.domain.usecase.GetCurrentUserUseCase
import dev.bonygod.gymroutine.core.navigation.BottomTab
import dev.bonygod.gymroutine.core.navigation.Navigator
import dev.bonygod.gymroutine.routines.domain.model.Routine
import dev.bonygod.gymroutine.routines.domain.model.withProgress
import dev.bonygod.gymroutine.routines.domain.usecase.GetRoutinesUseCase
import dev.bonygod.gymroutine.routines.domain.usecase.UpdateRoutineUseCase
import dev.bonygod.gymroutine.workout.domain.model.ExerciseSessionProgress
import dev.bonygod.gymroutine.workout.domain.model.WorkoutSession
import dev.bonygod.gymroutine.workout.domain.usecase.ClearWorkoutSessionUseCase
import dev.bonygod.gymroutine.workout.domain.usecase.GetWorkoutSessionUseCase
import dev.bonygod.gymroutine.workout.domain.usecase.LogWorkoutUseCase
import dev.bonygod.gymroutine.workout.domain.usecase.SaveWorkoutSessionUseCase
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
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

class WorkoutViewModel(
    private val navigator: Navigator,
    private val getCurrentUser: GetCurrentUserUseCase,
    private val logWorkout: LogWorkoutUseCase,
    private val getRoutines: GetRoutinesUseCase,
    private val updateRoutine: UpdateRoutineUseCase,
    private val getWorkoutSession: GetWorkoutSessionUseCase,
    private val saveWorkoutSession: SaveWorkoutSessionUseCase,
    private val clearWorkoutSession: ClearWorkoutSessionUseCase,
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
            is WorkoutEvent.OnSetCompleted -> onSetCompleted(event.index)
            is WorkoutEvent.OnToggleSkipExercise -> {
                setState { toggleSkipExercise(event.index) }
                persistSession()
            }
            is WorkoutEvent.OnSaveExerciseProgress -> scheduleSave(event.index)
            is WorkoutEvent.OnFinishWorkout -> finishWorkout(event.routineId, event.routineName)
            is WorkoutEvent.OnBackClick -> onBackClick()
        }
    }

    private fun onBackClick() {
        // Flush any pending debounced saves — and any pending session write — synchronously
        // before navigating away. navigator.goBack() can destroy this ViewModel and cancel
        // viewModelScope right after, so anything fired via a bare `launch` (persistSession())
        // could be cancelled mid-write; awaiting saveSession() here guarantees it completes first.
        viewModelScope.launch {
            cancelAndFlushSaveJobs()
            saveExerciseProgress()
            saveSession()
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

    // ── Sets & session ────────────────────────────────────────────────────────

    /**
     * Suma una serie. Cuando se alcanzan todas las series del ejercicio, éste se marca
     * como completado automáticamente — es la única vía de completar un ejercicio.
     */
    private fun onSetCompleted(index: Int) {
        val exercise = _state.value.exercises.getOrNull(index) ?: return
        val done = ((_state.value.completedSets[index] ?: 0) + 1).coerceAtMost(exercise.sets)
        setState { setCompletedSets(index, done) }
        if (exercise.sets > 0 && done >= exercise.sets) {
            setState { completeExercise(index) }
        }
        persistSession()
    }

    /**
     * Vuelca el progreso de la sesión actual (series completadas, ejercicios completados/omitidos).
     * Se llama en cada cambio poco frecuente de ese progreso — no lleva debounce, a diferencia de
     * [scheduleSave], porque no se dispara por pulsación de tecla.
     *
     * Fire-and-forget: no se espera desde el propio evento para no bloquear la UI. En los sitios
     * donde el ViewModel puede destruirse justo después (ver [onBackClick]) se llama en su lugar
     * a [saveSession] directamente, esperada, para que la escritura termine antes de navegar.
     */
    private fun persistSession() {
        val routineId = currentRoutine?.id ?: return
        if (userId.isEmpty()) return
        viewModelScope.launch { saveSession() }
    }

    /** Construye la sesión actual a partir del snapshot de estado en el momento de la llamada y la guarda. */
    private suspend fun saveSession() {
        val routineId = currentRoutine?.id ?: return
        if (userId.isEmpty()) return
        val s = _state.value
        val session = WorkoutSession(
            routineId = routineId,
            date = today(),
            exerciseCount = s.exercises.size,
            exercises = s.exercises.indices.map { i ->
                ExerciseSessionProgress(
                    index = i,
                    completedSets = s.completedSets[i] ?: 0,
                    isCompleted = i in s.completedExercises,
                    isSkipped = i in s.skippedExercises,
                )
            },
        )
        saveWorkoutSession(userId, session)
            .onFailure { e -> setEffect(WorkoutEffect.ShowError(e.message.orEmpty())) }
    }

    private fun today(): String = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date
        .toString()

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
                        getWorkoutSession(uid, routineId).onSuccess { session ->
                            // Descartar la sesión si la rutina se editó (cambió el nº de ejercicios)
                            // o si es de otro día: un entrenamiento pertenece a un día concreto.
                            if (session == null ||
                                session.exerciseCount != routine.exercises.size ||
                                session.date != today()
                            ) {
                                return@onSuccess
                            }
                            setState {
                                restoreSession(
                                    completed = session.exercises.filter { it.isCompleted }.map { it.index }.toSet(),
                                    skipped = session.exercises.filter { it.isSkipped }.map { it.index }.toSet(),
                                    sets = session.exercises.associate { it.index to it.completedSets },
                                )
                            }
                        }
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
            clearWorkoutSession(userId, routineId)
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
     *
     * IMPORTANT — do NOT call `setState { setExercises(...) }` here, and do NOT reassign
     * [currentRoutine], after a successful write. `_state.exercises`/`currentRoutine` must stay
     * the baseline loaded from Firestore for the whole session, so every debounced save recomputes
     * `withProgress` from that same baseline and stays idempotent: at most one new `history` entry
     * per exercise per session, even if the debounce fires 20 writes. Updating the baseline here
     * would make every save stack a new entry on top of the previous one instead of replacing it.
     */
    private suspend fun saveExerciseProgress() {
        val routine = currentRoutine ?: return
        if (userId.isEmpty()) return
        val currentState = _state.value
        val now = Clock.System.now().toEpochMilliseconds()
        val updatedExercises = currentState.exercises.mapIndexed { i, ex ->
            if (i in currentState.skippedExercises) return@mapIndexed ex
            val form = currentState.exerciseForms[i]
            val newWeight = form?.weight?.replace(',', '.')?.toFloatOrNull() ?: ex.weight
            val newReps = form?.reps?.toIntOrNull() ?: ex.reps
            ex.withProgress(newWeight, newReps, now)
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
