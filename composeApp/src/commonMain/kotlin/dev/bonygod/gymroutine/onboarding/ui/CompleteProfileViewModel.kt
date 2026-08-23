package dev.bonygod.gymroutine.onboarding.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bonygod.gymroutine.auth.domain.usecase.UpdateUserProfileUseCase
import dev.bonygod.gymroutine.core.navigation.Navigator
import dev.bonygod.gymroutine.core.navigation.Routes
import dev.bonygod.gymroutine.onboarding.ui.interactions.CompleteProfileEffect
import dev.bonygod.gymroutine.onboarding.ui.interactions.CompleteProfileEvent
import dev.bonygod.gymroutine.onboarding.ui.interactions.CompleteProfileState
import gymroutine.composeapp.generated.resources.Res
import gymroutine.composeapp.generated.resources.error_complete_profile_generic
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString

class CompleteProfileViewModel(
    private val navigator: Navigator,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(CompleteProfileState())
    val state: StateFlow<CompleteProfileState> = _state

    private val _effect = MutableSharedFlow<CompleteProfileEffect>(replay = 1)
    val effect: SharedFlow<CompleteProfileEffect> = _effect.asSharedFlow()

    private var userId: String = ""

    fun onEvent(event: CompleteProfileEvent) {
        when (event) {
            is CompleteProfileEvent.OnInit -> userId = event.userId
            is CompleteProfileEvent.OnAgeChange -> setState { setAge(event.value) }
            is CompleteProfileEvent.OnHeightChange -> setState { setHeight(event.value) }
            is CompleteProfileEvent.OnWeightChange -> setState { setWeight(event.value) }
            is CompleteProfileEvent.OnSaveClick -> saveProfile()
        }
    }

    private fun saveProfile() {
        if (userId.isEmpty()) return
        viewModelScope.launch {
            val currentState = state.value
            setState { showSaving(true) }
            updateUserProfileUseCase(
                uid = userId,
                age = currentState.age.toString(),
                weight = currentState.weight.toString(),
                height = currentState.height.toString(),
            ).onSuccess {
                setState { showSaving(false) }
                navigator.clearAndNavigateTo(Routes.Main(userId))
            }.onFailure { error ->
                setState { showSaving(false) }
                setEffect(CompleteProfileEffect.ShowError(error.message ?: getString(Res.string.error_complete_profile_generic)))
            }
        }
    }

    private fun setState(reducer: CompleteProfileState.() -> CompleteProfileState) {
        _state.value = _state.value.reducer()
    }

    private suspend fun setEffect(effect: CompleteProfileEffect) {
        _effect.emit(effect)
    }
}
