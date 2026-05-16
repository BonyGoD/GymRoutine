package dev.bonygod.gymroutine.auth.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bonygod.gymroutine.auth.domain.usecase.LoginUseCase
import dev.bonygod.gymroutine.auth.domain.usecase.LoginWithSocialProviderUseCase
import dev.bonygod.gymroutine.auth.domain.usecase.RegisterUseCase
import dev.bonygod.gymroutine.auth.domain.usecase.SendPasswordResetUseCase
import dev.bonygod.gymroutine.auth.ui.interactions.AuthEffect
import dev.bonygod.gymroutine.auth.ui.interactions.AuthEvent
import dev.bonygod.gymroutine.auth.ui.interactions.AuthState
import dev.bonygod.gymroutine.core.navigation.Navigator
import dev.bonygod.gymroutine.core.navigation.Routes
import gymroutine.composeapp.generated.resources.Res
import gymroutine.composeapp.generated.resources.error_invalid_email
import gymroutine.composeapp.generated.resources.error_name_empty
import gymroutine.composeapp.generated.resources.error_password_too_short
import gymroutine.composeapp.generated.resources.error_passwords_not_match
import gymroutine.composeapp.generated.resources.error_register_generic
import gymroutine.composeapp.generated.resources.error_reset_password_cooldown
import gymroutine.composeapp.generated.resources.error_reset_password_generic
import gymroutine.composeapp.generated.resources.error_sign_in_generic
import gymroutine.composeapp.generated.resources.error_social_get_credential
import gymroutine.composeapp.generated.resources.error_social_invalid_credential
import gymroutine.composeapp.generated.resources.error_social_no_google_account
import gymroutine.composeapp.generated.resources.error_social_sign_in_generic
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes

class AuthViewModel(
    private val navigator: Navigator,
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val sendPasswordResetUseCase: SendPasswordResetUseCase,
    private val loginWithSocialProviderUseCase: LoginWithSocialProviderUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state

    private val _effect = MutableSharedFlow<AuthEffect>(replay = 1)
    val effect: SharedFlow<AuthEffect> = _effect.asSharedFlow()

    private var lastResetRequestTime: Long = 0L

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.OnUserNameChange -> setState { updateUserName(event.value) }
            is AuthEvent.OnEmailChange -> setState { updateEmail(event.value) }
            is AuthEvent.OnPasswordChange -> setState { updatePassword(event.value) }
            is AuthEvent.OnConfirmPasswordChange -> setState { updateConfirmPassword(event.value) }
            is AuthEvent.OnEyePasswordClick -> setState { updateEyePassword() }
            is AuthEvent.OnEyeConfirmPasswordClick -> setState { updateEyeConfirmPassword() }
            is AuthEvent.OnSignInClick -> signIn()
            is AuthEvent.OnRegisterClick -> register()
            is AuthEvent.OnResetPassword -> sendPasswordReset(event.value)
            is AuthEvent.OnGoogleSignInSuccess -> loginWithSocialProvider(event.uid, event.displayName, event.email)
            is AuthEvent.OnGoogleSignInError -> handleGoogleSignInError(event.errorMessage)
            is AuthEvent.OnNavigateToRegister -> navigator.navigateTo(Routes.Register)
            is AuthEvent.OnNavigateToForgotPassword -> navigator.navigateTo(Routes.ForgotPassword)
            is AuthEvent.DismissDialog -> setState { showDialog(false) }
            is AuthEvent.OnBackClick -> navigator.goBack()
        }
    }

    private fun signIn() {
        viewModelScope.launch {
            val currentState = state.value
            if (!currentState.isValidEmail()) {
                setEffect(AuthEffect.ShowError(getString(Res.string.error_invalid_email)))
                return@launch
            }
            val (_, email, password, _) = currentState.getUserData()
            setState { showLoading(true) }
            loginUseCase(email = email, password = password)
                .onSuccess { user ->
                    setState { showLoading(false) }
                    navigator.clearAndNavigateTo(Routes.Main(user.uid))
                }
                .onFailure { error ->
                    setState { showLoading(false) }
                    setEffect(AuthEffect.ShowError(error.message ?: getString(Res.string.error_sign_in_generic)))
                }
        }
    }

    private fun register() {
        viewModelScope.launch {
            val currentState = state.value
            val validationError = when {
                !currentState.isNameFilled() -> getString(Res.string.error_name_empty)
                !currentState.isValidEmail() -> getString(Res.string.error_invalid_email)
                !currentState.passwordsMatch() -> getString(Res.string.error_passwords_not_match)
                !currentState.isPasswordLongEnough() -> getString(Res.string.error_password_too_short)
                else -> null
            }
            if (validationError != null) {
                setEffect(AuthEffect.ShowError(validationError))
                return@launch
            }
            val (name, email, password, _) = currentState.getUserData()
            setState { showLoading(true) }
            registerUseCase(
                email = email,
                password = password,
                name = name,
                age = "",
                weight = "",
                height = "",
            ).onSuccess { user ->
                setState { showLoading(false) }
                navigator.clearAndNavigateTo(Routes.Main(user.uid))
            }.onFailure { error ->
                setState { showLoading(false) }
                setEffect(AuthEffect.ShowError(error.message ?: getString(Res.string.error_register_generic)))
            }
        }
    }

    private fun sendPasswordReset(email: String) {
        viewModelScope.launch {
            val now = Clock.System.now().toEpochMilliseconds()
            if (now - lastResetRequestTime < 5.minutes.inWholeMilliseconds) {
                setEffect(AuthEffect.ShowError(getString(Res.string.error_reset_password_cooldown)))
                return@launch
            }
            sendPasswordResetUseCase(email = email)
                .onSuccess {
                    lastResetRequestTime = Clock.System.now().toEpochMilliseconds()
                    setState { updateEmail("").showDialog(true) }
                }
                .onFailure { error ->
                    setEffect(AuthEffect.ShowError(error.message ?: getString(Res.string.error_reset_password_generic)))
                }
        }
    }

    private fun loginWithSocialProvider(uid: String, displayName: String, email: String) {
        viewModelScope.launch {
            setState { showLoading(true) }
            loginWithSocialProviderUseCase(uid = uid, displayName = displayName, email = email)
                .onSuccess { user ->
                    setState { showLoading(false) }
                    navigator.clearAndNavigateTo(Routes.Main(user.uid))
                }
                .onFailure { error ->
                    setState { showLoading(false) }
                    setEffect(AuthEffect.ShowError(error.message ?: getString(Res.string.error_social_sign_in_generic)))
                }
        }
    }

    private fun handleGoogleSignInError(errorMessage: String) {
        viewModelScope.launch {
            setEffect(AuthEffect.ShowError(translateSocialError(errorMessage)))
        }
    }

    private fun setState(reducer: AuthState.() -> AuthState) {
        _state.value = _state.value.reducer()
    }

    private suspend fun setEffect(effect: AuthEffect) {
        _effect.emit(effect)
    }

    private suspend fun translateSocialError(message: String): String = when {
        message.contains("No email found in your device", ignoreCase = true) ->
            getString(Res.string.error_social_no_google_account)
        message.contains("Invalid credential type", ignoreCase = true) ->
            getString(Res.string.error_social_invalid_credential)
        message.contains("GetCredentialException", ignoreCase = true) ->
            getString(Res.string.error_social_get_credential)
        message.contains("Error:", ignoreCase = true) ->
            getString(Res.string.error_social_sign_in_generic)
        else -> message
    }
}
