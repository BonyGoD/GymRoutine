package org.bonygod.gymroutine.ui.view.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.gitlive.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _dialogViewModel = MutableStateFlow(DialogViewModel())
    val dialogViewModel = _dialogViewModel.asStateFlow()

    private val _passwordVisible = MutableStateFlow(false)
    val passwordVisible = _passwordVisible.asStateFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    fun onPasswordVisibleChange(passwordVisible: Boolean) {
        _passwordVisible.value = passwordVisible
    }

    fun onEmailChange(email: String) {
        _email.value = email
    }

    fun onPasswordChange(password: String) {
        _password.value = password
    }

    fun signIn(auth: FirebaseAuth, navigateToPrimeraPantalla: () -> Unit) {
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email.value, password.value)
                navigateToPrimeraPantalla()
            } catch (e: Exception) {
                dialogViewModel.value.setCustomDialog(
                    dialogViewModel.value.customDialogTitle.value,
                    "",
                    dialogViewModel.value.icon.value,
                    dialogViewModel.value.iconColor.value
                )
                dialogViewModel.value.onShowDialogChange(true)
            }
        }
    }

    fun onForgotPasswordClick(navigateToForgotScreen: () -> Unit) {
        navigateToForgotScreen()
    }
}