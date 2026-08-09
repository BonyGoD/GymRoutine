package dev.bonygod.gymroutine.evolution.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bonygod.gymroutine.auth.domain.usecase.GetCurrentUserUseCase
import dev.bonygod.gymroutine.evolution.domain.usecase.GetExerciseEvolutionsUseCase
import dev.bonygod.gymroutine.evolution.domain.usecase.SeedEvolutionDataUseCase
import dev.bonygod.gymroutine.evolution.ui.interactions.EvolutionEffect
import dev.bonygod.gymroutine.evolution.ui.interactions.EvolutionEvent
import dev.bonygod.gymroutine.evolution.ui.interactions.EvolutionState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class EvolutionViewModel(
    private val getCurrentUser: GetCurrentUserUseCase,
    private val getExerciseEvolutions: GetExerciseEvolutionsUseCase,
    private val seedEvolutionData: SeedEvolutionDataUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(EvolutionState())
    val state: StateFlow<EvolutionState> = _state

    private val _effect = MutableSharedFlow<EvolutionEffect>(replay = 1)
    val effect: SharedFlow<EvolutionEffect> = _effect.asSharedFlow()

    fun onEvent(event: EvolutionEvent) {
        when (event) {
            is EvolutionEvent.OnInit -> loadEvolutions()
            is EvolutionEvent.OnSelectExercise -> setState { select(event.evolution) }
            is EvolutionEvent.OnDismissDetail -> setState { dismissDetail() }
            is EvolutionEvent.OnSeedTestData -> seedTestData()
        }
    }

    private fun loadEvolutions() {
        viewModelScope.launch {
            setState { showLoading(true) }
            getCurrentUser()
                .onSuccess { user ->
                    val uid = user?.uid.orEmpty()
                    setState { setCurrentUserId(uid) }
                    if (uid.isEmpty()) {
                        setState { showLoading(false) }
                        return@onSuccess
                    }
                    getExerciseEvolutions(uid)
                        .onSuccess { evolutions -> setState { showLoading(false).setEvolutions(evolutions) } }
                        .onFailure { e ->
                            setState { showLoading(false) }
                            setEffect(EvolutionEffect.ShowError(e.message.orEmpty()))
                        }
                }
                .onFailure { e ->
                    setState { showLoading(false) }
                    setEffect(EvolutionEffect.ShowError(e.message.orEmpty()))
                }
        }
    }

    private fun seedTestData() {
        val uid = _state.value.currentUserId
        if (uid.isEmpty()) return
        viewModelScope.launch {
            seedEvolutionData(uid)
                .onSuccess { loadEvolutions() }
                .onFailure { e -> setEffect(EvolutionEffect.ShowError(e.message.orEmpty())) }
        }
    }

    private fun setState(reducer: EvolutionState.() -> EvolutionState) {
        _state.value = _state.value.reducer()
    }

    private suspend fun setEffect(effect: EvolutionEffect) {
        _effect.emit(effect)
    }
}
