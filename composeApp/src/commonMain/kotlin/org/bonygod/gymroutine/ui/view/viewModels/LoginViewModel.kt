package org.bonygod.gymroutine.ui.view.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.bonygod.gymroutine.domain.LoginUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LoginViewModel : ViewModel(), KoinComponent {

    private val loginUseCase: LoginUseCase by inject()

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

    fun signIn(navigateToPrimeraPantalla: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val result = loginUseCase(email.value, password.value)
                if (result.idToken != null) {
                    Result.success(result.idToken)
                    navigateToPrimeraPantalla(result.displayName.toString())
                } else {
                    Result.failure<Exception>(Exception(result.error?.message ?: "Error desconocido"))
                }
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