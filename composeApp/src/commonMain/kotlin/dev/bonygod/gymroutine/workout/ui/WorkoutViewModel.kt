package dev.bonygod.gymroutine.workout.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bonygod.gymroutine.auth.domain.usecase.GetCurrentUserUseCase
import dev.bonygod.gymroutine.core.navigation.Navigator
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
) : ViewModel() {

    private val _state = MutableStateFlow(WorkoutState())
    val state: StateFlow<WorkoutState> = _state

    private val _effect = MutableSharedFlow<WorkoutEffect>(replay = 1)
    val effect: SharedFlow<WorkoutEffect> = _effect.asSharedFlow()

    private var userId: String = ""

    init {
        viewModelScope.launch {
            getCurrentUser().onSuccess { user ->
                userId = user?.uid.orEmpty()
            }
        }
    }

    fun onEvent(event: WorkoutEvent) {
        when (event) {
            is WorkoutEvent.OnStartWorkout -> setState { start() }
            is WorkoutEvent.OnFinishWorkout -> finishWorkout(event.routineId, event.routineName)
            is WorkoutEvent.OnBackClick -> navigator.goBack()
        }
    }

    private fun finishWorkout(routineId: String, routineName: String) {
        if (userId.isEmpty()) return
        viewModelScope.launch {
            setState { showLogging() }
            runCatching { logWorkout(userId, routineId, routineName) }
                .onFailure { e -> setEffect(WorkoutEffect.ShowError(e.message.orEmpty())) }
            navigator.goBack()
        }
    }

    private fun setState(reducer: WorkoutState.() -> WorkoutState) {
        _state.value = _state.value.reducer()
    }

    private suspend fun setEffect(effect: WorkoutEffect) {
        _effect.emit(effect)
    }
}
