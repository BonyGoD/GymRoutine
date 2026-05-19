package dev.bonygod.gymroutine.routines.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bonygod.gymroutine.auth.domain.usecase.GetCurrentUserUseCase
import dev.bonygod.gymroutine.core.navigation.Navigator
import dev.bonygod.gymroutine.core.navigation.Routes
import dev.bonygod.gymroutine.routines.domain.usecase.CreateRoutineUseCase
import dev.bonygod.gymroutine.routines.domain.usecase.DeleteRoutineUseCase
import dev.bonygod.gymroutine.routines.domain.usecase.GetRoutinesUseCase
import dev.bonygod.gymroutine.routines.domain.usecase.UpdateRoutineUseCase
import dev.bonygod.gymroutine.routines.ui.interactions.RoutinesEffect
import dev.bonygod.gymroutine.routines.ui.interactions.RoutinesEvent
import dev.bonygod.gymroutine.routines.ui.interactions.RoutinesState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class RoutinesViewModel(
    private val navigator: Navigator,
    private val getCurrentUser: GetCurrentUserUseCase,
    private val getRoutines: GetRoutinesUseCase,
    private val createRoutine: CreateRoutineUseCase,
    private val updateRoutine: UpdateRoutineUseCase,
    private val deleteRoutine: DeleteRoutineUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(RoutinesState())
    val state: StateFlow<RoutinesState> = _state

    private val _effect = MutableSharedFlow<RoutinesEffect>(replay = 1)
    val effect: SharedFlow<RoutinesEffect> = _effect.asSharedFlow()

    private var currentUserId: String = ""

    init {
        loadRoutines()
    }

    fun onEvent(event: RoutinesEvent) {
        when (event) {
            is RoutinesEvent.OnLoadRoutines -> loadRoutines()
            is RoutinesEvent.OnCreateRoutine -> createRoutine(event.routine)
            is RoutinesEvent.OnUpdateRoutine -> updateRoutine(event.routine)
            is RoutinesEvent.OnDeleteRoutine -> deleteRoutine(event.routineId)
            is RoutinesEvent.OnNavigateToAddRoutine -> navigator.navigateTo(Routes.AddRoutine)
            is RoutinesEvent.OnNavigateToEditRoutine -> navigator.navigateTo(Routes.EditRoutine(event.routineId))
            is RoutinesEvent.OnBackClick -> navigator.goBack()
        }
    }

    private fun loadRoutines() {
        viewModelScope.launch {
            setState { showLoading(true) }
            getCurrentUser()
                .onSuccess { user ->
                    currentUserId = user?.uid.orEmpty()
                    getRoutines(currentUserId)
                        .onSuccess { routines -> setState { showLoading(false).setRoutines(routines) } }
                        .onFailure { e ->
                            setState { showLoading(false) }
                            setEffect(RoutinesEffect.ShowError(e.message.orEmpty()))
                        }
                }
                .onFailure { e ->
                    setState { showLoading(false) }
                    setEffect(RoutinesEffect.ShowError(e.message.orEmpty()))
                }
        }
    }

    private fun deleteRoutine(routineId: String) {
        viewModelScope.launch {
            deleteRoutine(currentUserId, routineId)
                .onSuccess { loadRoutines() }
                .onFailure { e -> setEffect(RoutinesEffect.ShowError(e.message.orEmpty())) }
        }
    }

    private fun createRoutine(routine: dev.bonygod.gymroutine.routines.domain.model.Routine) {
        viewModelScope.launch {
            createRoutine(currentUserId, routine)
                .onSuccess {
                    loadRoutines()
                    navigator.goBack()
                }
                .onFailure { e -> setEffect(RoutinesEffect.ShowError(e.message.orEmpty())) }
        }
    }

    private fun updateRoutine(routine: dev.bonygod.gymroutine.routines.domain.model.Routine) {
        viewModelScope.launch {
            updateRoutine(currentUserId, routine)
                .onSuccess {
                    loadRoutines()
                    navigator.goBack()
                }
                .onFailure { e -> setEffect(RoutinesEffect.ShowError(e.message.orEmpty())) }
        }
    }

    private fun setState(reducer: RoutinesState.() -> RoutinesState) {
        _state.value = _state.value.reducer()
    }

    private suspend fun setEffect(effect: RoutinesEffect) {
        _effect.emit(effect)
    }
}
