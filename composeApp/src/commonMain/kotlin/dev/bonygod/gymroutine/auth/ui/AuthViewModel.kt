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
    private val firebaseAuth: FirebaseAuth
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
            is AuthEvent.OnGoogleSignInError -> setEffect(AuthEffect.ShowError(event.errorMessage))
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
                setEffect(AuthEffect.ShowError(error.message ?: "Sign in failed"))
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
                setEffect(AuthEffect.ShowError(error.message ?: "Registration failed"))
            }
        }
    }

    private fun handleGoogleSignIn(uid: String, displayName: String, email: String) {
        navigator.clearAndNavigateTo(Routes.Main(uid))
    }

    private fun resetPassword(email: String) {
        val currentTime = Clock.System.now().toEpochMilliseconds()
        if (currentTime - lastResetRequestTime < 5.minutes.inWholeMilliseconds) {
            setEffect(AuthEffect.ShowError("Please wait 5 minutes before retrying."))
            return
        }
        viewModelScope.launch {
            runCatching { firebaseAuth.sendPasswordResetEmail(email) }
                .onSuccess {
                    lastResetRequestTime = Clock.System.now().toEpochMilliseconds()
                    setState { updateEmail("").showDialog(true) }
                }
                .onFailure { error ->
                    setEffect(AuthEffect.ShowError(error.message ?: "Reset failed"))
                }
        }
    }
}
