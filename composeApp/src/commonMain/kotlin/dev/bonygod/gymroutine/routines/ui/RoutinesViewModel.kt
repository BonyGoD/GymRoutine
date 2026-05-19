package dev.bonygod.gymroutine.routines.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bonygod.gymroutine.auth.domain.usecase.GetCurrentUserUseCase
import dev.bonygod.gymroutine.routines.domain.model.Routine
import dev.bonygod.gymroutine.routines.domain.usecase.CreateRoutineUseCase
import dev.bonygod.gymroutine.routines.domain.usecase.DeleteRoutineUseCase
import dev.bonygod.gymroutine.routines.domain.usecase.GetRoutinesUseCase
import dev.bonygod.gymroutine.routines.domain.usecase.UpdateRoutineUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface RoutinesUiState {
    data object Loading : RoutinesUiState
    data class Success(val routines: List<Routine>) : RoutinesUiState
    data class Error(val message: String) : RoutinesUiState
}

class RoutinesViewModel(
    private val getCurrentUser: GetCurrentUserUseCase,
    private val getRoutines: GetRoutinesUseCase,
    private val createRoutine: CreateRoutineUseCase,
    private val updateRoutine: UpdateRoutineUseCase,
    private val deleteRoutine: DeleteRoutineUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<RoutinesUiState>(RoutinesUiState.Loading)
    val uiState: StateFlow<RoutinesUiState> = _uiState.asStateFlow()

    private var currentUserId: String = ""

    init {
        loadRoutines()
    }

    private fun loadRoutines() {
        viewModelScope.launch {
            _uiState.value = RoutinesUiState.Loading
            getCurrentUser()
                .onSuccess { user ->
                    currentUserId = user?.uid.orEmpty()
                    getRoutines(currentUserId)
                        .onSuccess { routines -> _uiState.value = RoutinesUiState.Success(routines) }
                        .onFailure { e -> _uiState.value = RoutinesUiState.Error(e.message.orEmpty()) }
                }
                .onFailure { e -> _uiState.value = RoutinesUiState.Error(e.message.orEmpty()) }
        }
    }

    fun onDeleteRoutine(routineId: String) {
        viewModelScope.launch {
            deleteRoutine(currentUserId, routineId)
                .onSuccess { loadRoutines() }
                .onFailure { e -> _uiState.value = RoutinesUiState.Error(e.message.orEmpty()) }
        }
    }

    fun onCreateRoutine(routine: Routine) {
        viewModelScope.launch {
            createRoutine(currentUserId, routine)
                .onSuccess { loadRoutines() }
                .onFailure { e -> _uiState.value = RoutinesUiState.Error(e.message.orEmpty()) }
        }
    }

    fun onUpdateRoutine(routine: Routine) {
        viewModelScope.launch {
            updateRoutine(currentUserId, routine)
                .onSuccess { loadRoutines() }
                .onFailure { e -> _uiState.value = RoutinesUiState.Error(e.message.orEmpty()) }
        }
    }
}
