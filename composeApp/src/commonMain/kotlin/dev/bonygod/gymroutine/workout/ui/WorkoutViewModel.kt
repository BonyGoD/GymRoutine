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
                if (event.index !in _state.value.completedExercises) {
                    setState { toggleExpanded(event.index) }
                }
            }
            is WorkoutEvent.OnUpdateWeight -> setState { updateWeight(event.index, event.weight) }
            is WorkoutEvent.OnUpdateReps -> setState { updateReps(event.index, event.reps) }
            is WorkoutEvent.OnCompleteExercise -> setState { completeExercise(event.index) }
            is WorkoutEvent.OnFinishWorkout -> finishWorkout(event.routineId, event.routineName)
            is WorkoutEvent.OnBackClick -> navigator.goBack()
        }
    }

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

    private fun finishWorkout(routineId: String, routineName: String) {
        if (userId.isEmpty()) return
        viewModelScope.launch {
            setState { showLogging() }
            saveExerciseProgress()
            runCatching { logWorkout(userId, routineId, routineName, completado = true) }
                .onFailure { e -> setEffect(WorkoutEffect.ShowError(e.message.orEmpty())) }
            navigator.currentTab.value = BottomTab.Home
            navigator.goBack()
        }
    }

    /** Persiste en Firestore el peso/reps modificados; initialWeight/initialReps no se tocan. */
    private suspend fun saveExerciseProgress() {
        val routine = currentRoutine ?: return
        val currentState = _state.value
        val updatedExercises = currentState.exercises.mapIndexed { i, ex ->
            val form = currentState.exerciseForms[i]
            val newWeight = form?.weight?.toFloatOrNull() ?: ex.weight
            val newReps = form?.reps?.toIntOrNull() ?: ex.reps
            ex.copy(weight = newWeight, reps = newReps)
        }
        if (updatedExercises != currentState.exercises) {
            runCatching { updateRoutine(userId, routine.copy(exercises = updatedExercises)) }
        }
    }

    private fun setState(reducer: WorkoutState.() -> WorkoutState) {
        _state.value = _state.value.reducer()
    }

    private suspend fun setEffect(effect: WorkoutEffect) {
        _effect.emit(effect)
    }
}
