package org.bonygod.gymroutine.ui.view.viewModels

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.bonygod.gymroutine.data.model.AuthResult
import org.bonygod.gymroutine.data.model.User
import org.bonygod.gymroutine.domain.SignUpUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SignUpViewModel: ViewModel(), KoinComponent {

    private val signUpUseCase: SignUpUseCase by inject()

    private val userViewModel: UserViewModel by inject()
    //val userDb = userViewModel.getUser()

    private val _dialogViewModel = MutableStateFlow(DialogViewModel())
    val dialogViewModel = _dialogViewModel.asStateFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _user = MutableStateFlow("")
    val user = _user.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _passwordVisible = MutableStateFlow(false)
    val passwordVisible = _passwordVisible.asStateFlow()

    private val _passwordVisibleRepeat = MutableStateFlow(false)
    val passwordVisibleRepeat = _passwordVisibleRepeat.asStateFlow()

    private val _passwordRepeat = MutableStateFlow("")
    val passwordRepeat = _passwordRepeat.asStateFlow()

    private val _colorFirstTextField = MutableStateFlow(Color.Black)
    val colorFirstTextField = _colorFirstTextField.asStateFlow()

    private val _colorSecondTextField = MutableStateFlow(Color.Black)
    val colorSecondTextField = _colorSecondTextField.asStateFlow()

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
        updateTextFieldColors()
    }

    fun onPasswordRepeatChange(newPasswordRepeat: String) {
        _passwordRepeat.value = newPasswordRepeat
        updateTextFieldColors()
    }

    fun onPasswordVisibleRepeatChange(passwordVisibleRepeat: Boolean) {
        _passwordVisibleRepeat.value = passwordVisibleRepeat
    }

    fun onEmailChange(email: String) {
        _email.value = email
    }

    fun onUserChange(user: String) {
        _user.value = user
    }

    fun onPasswordVisibleChange(passwordVisible: Boolean) {
        _passwordVisible.value = passwordVisible
    }

    private fun updateTextFieldColors() {
        if (password.value != passwordRepeat.value) {
            _colorFirstTextField.value = Color.Red
            _colorSecondTextField.value = Color.Red
        } else {
            _colorFirstTextField.value = Color.Black
            _colorSecondTextField.value = Color.Black
        }
    }

    fun signUp(navigateToPrimeraPantalla: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val result = signUpUseCase(email.value, password.value, user.value)
                if (result.idToken != null) {
                    Result.success(result.idToken)
                    userViewModel.insertUser(createUser(result))
                    _user.value = result.displayName.toString()
                    navigateToPrimeraPantalla(_user.value)
                } else {
                    Result.failure<Exception>(Exception(result.error?.message ?: "Error desconocido"))
                }
            } catch (e: Exception) {
                dialogViewModel.value.setCustomDialog(
                    dialogViewModel.value.customDialogTitle.value,
                    dialogViewModel.value.customDialogSubtitle.value,
                    dialogViewModel.value.icon.value,
                    dialogViewModel.value.iconColor.value
                )
                dialogViewModel.value.onShowDialogChange(true)
            }
        }
    }

    private fun createUser(result: AuthResult): User {
        return User(
            id = result.localId.toString(),
            displayName = result.displayName.toString(),
            email = result.email.toString(),
            token = result.idToken.toString()
        )
    }
}