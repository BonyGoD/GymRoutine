package dev.bonygod.gymroutine.auth.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bonygod.gymroutine.auth.domain.usecase.ResolveSessionUseCase
import dev.bonygod.gymroutine.auth.ui.interactions.SplashEvent
import dev.bonygod.gymroutine.auth.ui.interactions.SplashState
import dev.bonygod.gymroutine.core.navigation.Navigator
import dev.bonygod.gymroutine.core.navigation.Routes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SplashViewModel(
    private val navigator: Navigator,
    private val resolveSessionUseCase: ResolveSessionUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(SplashState())
    val state: StateFlow<SplashState> = _state

    init {
        resolveSession()
    }

    fun onEvent(event: SplashEvent) {
        when (event) {
            is SplashEvent.OnRetry -> resolveSession()
            is SplashEvent.OnGoToLogin -> navigator.clearAndNavigateTo(Routes.Login)
        }
    }

    private fun resolveSession() {
        setState { showLoading() }
        viewModelScope.launch {
            resolveSessionUseCase().fold(
                onSuccess = { user ->
                    if (user != null) {
                        navigator.navigateAfterAuth(user)
                    } else {
                        navigator.clearAndNavigateTo(Routes.Login)
                    }
                },
                onFailure = { setState { showError() } },
            )
        }
    }

    private fun setState(reducer: SplashState.() -> SplashState) {
        _state.value = _state.value.reducer()
    }
}
