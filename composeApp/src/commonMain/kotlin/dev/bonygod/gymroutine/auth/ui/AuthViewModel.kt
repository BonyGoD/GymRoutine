package dev.bonygod.gymroutine.auth.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bonygod.gymroutine.auth.ui.interactions.AuthEffect
import dev.bonygod.gymroutine.auth.ui.interactions.AuthEvent
import dev.bonygod.gymroutine.auth.ui.interactions.AuthState
import dev.bonygod.gymroutine.core.navigation.Navigator
import dev.bonygod.gymroutine.core.navigation.Routes
import dev.gitlive.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes

class AuthViewModel(
    private val navigator: Navigator,
    private val firebaseAuth: FirebaseAuth,
) : ViewModel() {
    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state

    private val _effect = MutableSharedFlow<AuthEffect>(replay = 1)
    val effect: SharedFlow<AuthEffect> = _effect.asSharedFlow()

    private var lastResetRequestTime: Long = 0L

    fun setState(reducer: AuthState.() -> AuthState) {
        _state.value = _state.value.reducer()
    }

    private fun setEffect(effect: AuthEffect) {
        viewModelScope.launch { _effect.emit(effect) }
    }

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.OnUserNameChange -> setState { updateUserName(event.value) }
            is AuthEvent.OnEmailChange -> setState { updateEmail(event.value) }
            is AuthEvent.OnPasswordChange -> setState { updatePassword(event.value) }
            is AuthEvent.OnConfirmPasswordChange -> setState { updateConfirmPassword(event.value) }
            is AuthEvent.OnEyePasswordClick -> setState { updateEyePassword() }
            is AuthEvent.OnEyeConfirmPasswordClick -> setState { updateEyeConfirmPassword() }
            is AuthEvent.OnResetPassword -> resetPassword(event.value)
            is AuthEvent.OnSignInClick -> signIn()
            is AuthEvent.OnRegisterClick -> register()
            is AuthEvent.OnGoogleSignInSuccess -> handleGoogleSignIn(event.uid, event.displayName, event.email)
            is AuthEvent.OnGoogleSignInError -> setEffect(AuthEffect.ShowError(translateSocialError(event.errorMessage)))
            is AuthEvent.OnNavigateToRegister -> navigator.navigateTo(Routes.Register)
            is AuthEvent.OnNavigateToForgotPassword -> navigator.navigateTo(Routes.ForgotPassword)
            is AuthEvent.DismissDialog -> setState { showDialog(false) }
            is AuthEvent.OnBackClick -> navigator.goBack()
        }
    }

    private fun signIn() {
        viewModelScope.launch {
            setState { showLoading(true) }
            runCatching {
                val user = state.value.getUserData()
                firebaseAuth.signInWithEmailAndPassword(user.email, user.password)
            }.onSuccess { result ->
                setState { showLoading(false) }
                val uid = result.user?.uid ?: ""
                navigator.clearAndNavigateTo(Routes.Main(uid))
            }.onFailure { error ->
                setState { showLoading(false) }
                setEffect(AuthEffect.ShowError(error.message ?: "Error al iniciar sesion"))
            }
        }
    }

    private fun register() {
        viewModelScope.launch {
            setState { showLoading(true) }
            runCatching {
                val user = state.value.getUserData()
                firebaseAuth.createUserWithEmailAndPassword(user.email, user.password)
            }.onSuccess { result ->
                setState { showLoading(false) }
                val uid = result.user?.uid ?: ""
                navigator.clearAndNavigateTo(Routes.Main(uid))
            }.onFailure { error ->
                setState { showLoading(false) }
                setEffect(AuthEffect.ShowError(error.message ?: "Error al registrarse"))
            }
        }
    }

    private fun handleGoogleSignIn(
        uid: String,
        displayName: String,
        email: String,
    ) {
        navigator.clearAndNavigateTo(Routes.Main(uid))
    }

    private fun resetPassword(email: String) {
        val currentTime = Clock.System.now().toEpochMilliseconds()
        if (currentTime - lastResetRequestTime < 5.minutes.inWholeMilliseconds) {
            setEffect(AuthEffect.ShowError("Espera 5 minutos antes de volver a intentarlo."))
            return
        }
        viewModelScope.launch {
            runCatching { firebaseAuth.sendPasswordResetEmail(email) }
                .onSuccess {
                    lastResetRequestTime = Clock.System.now().toEpochMilliseconds()
                    setState { updateEmail("").showDialog(true) }
                }.onFailure { error ->
                    setEffect(AuthEffect.ShowError(error.message ?: "Error al restablecer"))
                }
        }
    }

    private fun translateSocialError(message: String): String = when {
        message.contains("No email found in your device", ignoreCase = true) -> "No se encontro una cuenta de Google en el dispositivo"
        message.contains("Invalid credential type", ignoreCase = true) -> "Credencial de Google no valida"
        message.contains("GetCredentialException", ignoreCase = true) -> "Error al obtener credenciales de Google"
        message.contains("Error:", ignoreCase = true) -> "Error de inicio de sesion social"
        else -> message
    }
}
