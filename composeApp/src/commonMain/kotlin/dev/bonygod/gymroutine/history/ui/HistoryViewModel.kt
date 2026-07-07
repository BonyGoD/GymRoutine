package dev.bonygod.gymroutine.history.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bonygod.gymroutine.auth.domain.usecase.GetCurrentUserUseCase
import dev.bonygod.gymroutine.history.ui.interactions.HistoryEffect
import dev.bonygod.gymroutine.history.ui.interactions.HistoryEvent
import dev.bonygod.gymroutine.history.ui.interactions.HistoryState
import dev.bonygod.gymroutine.workout.domain.usecase.ObserveWorkoutLogsUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val getCurrentUser: GetCurrentUserUseCase,
    private val observeWorkoutLogs: ObserveWorkoutLogsUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(HistoryState())
    val state: StateFlow<HistoryState> = _state

    private val _effect = MutableSharedFlow<HistoryEffect>(replay = 1)
    val effect: SharedFlow<HistoryEffect> = _effect.asSharedFlow()

    init {
        load()
    }

    fun onEvent(event: HistoryEvent) {
        when (event) {
            is HistoryEvent.OnRefresh -> load()
        }
    }

    private fun load() {
        viewModelScope.launch {
            setState { setLoading() }
            getCurrentUser()
                .onSuccess { user ->
                    val uid = user?.uid.orEmpty()
                    if (uid.isEmpty()) {
                        setState { setLogs(emptyList()) }
                        return@onSuccess
                    }
                    observeWorkoutLogs(uid)
                        .catch { setState { setLogs(emptyList()) } }
                        .collect { logs ->
                            val completed = logs
                                .filter { it.completado }
                                .sortedByDescending { it.date }
                            setState { setLogs(completed) }
                        }
                }
                .onFailure { e ->
                    setState { setLogs(emptyList()) }
                    _effect.emit(HistoryEffect.ShowError(e.message.orEmpty()))
                }
        }
    }

    private fun setState(reducer: HistoryState.() -> HistoryState) {
        _state.value = _state.value.reducer()
    }
}
