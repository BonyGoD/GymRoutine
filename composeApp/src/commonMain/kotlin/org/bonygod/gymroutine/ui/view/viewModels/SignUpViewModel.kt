package org.bonygod.gymroutine.ui.view.viewModels

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gymroutine.composeapp.generated.resources.Res
import gymroutine.composeapp.generated.resources.exclamation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.bonygod.gymroutine.data.model.AuthResult
import org.bonygod.gymroutine.data.model.User
import org.bonygod.gymroutine.domain.SignUpUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SignUpViewModel : ViewModel(), KoinComponent {

    private val signUpUseCase: SignUpUseCase by inject()

    private val userViewModel: UserViewModel by inject()

    private val _dialogViewModel = MutableStateFlow(DialogViewModel())
    val dialogViewModel = _dialogViewModel.asStateFlow()

    private val _customDialogErrorTitle = MutableStateFlow("")
    val customDialogErrorTitle = _customDialogErrorTitle.asStateFlow()

    private val _customDialogErrorSubtitle = MutableStateFlow("")
    val customDialogErrorSubtitle = _customDialogErrorSubtitle.asStateFlow()

    private val _customDialogTitle = MutableStateFlow("")
    val customDialogTitle = _customDialogTitle.asStateFlow()

    private val _customDialogSubtitle = MutableStateFlow("")
    val customDialogSubtitle = _customDialogSubtitle.asStateFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _validEmail = MutableStateFlow(false)
    val validEmail = _validEmail.asStateFlow()

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

    private val _buttonVisible = MutableStateFlow(false)
    val buttonVisible = _buttonVisible.asStateFlow()

    fun validEmail(validEmail: Boolean) {
        _validEmail.value = validEmail
    }

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

    fun checkFields() {
        _buttonVisible.value = user.value.isNotEmpty()
                && validEmail.value
                && password.value.length >= 6
                && passwordRepeat.value.length >= 6
                && password.value == passwordRepeat.value
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

    fun setTitleErrorEmailExists(title: String, subtitle: String) {
        _customDialogErrorTitle.value = title
        _customDialogErrorSubtitle.value = subtitle
    }

    fun setTitlesGenericErrorDialog(title: String, subtitle: String) {
        _customDialogTitle.value = title
        _customDialogSubtitle.value = subtitle
    }

    fun signUp(dialogViewModel: DialogViewModel, navigateToPrimeraPantalla: () -> Unit) {
        viewModelScope.launch {
            try {
                val result = signUpUseCase(email.value, password.value, user.value)
                if (result.error != null) throw Exception(result.error.error.message)
                userViewModel.insertUser(createUser(result))
                navigateToPrimeraPantalla()
            } catch (e: Exception) {
                dialogViewModel.setCustomDialog(
                    if (e.message == "EMAIL_EXISTS") customDialogErrorTitle.value else dialogViewModel.customDialogTitle.value,
                    if (e.message == "EMAIL_EXISTS") customDialogErrorSubtitle.value else dialogViewModel.customDialogSubtitle.value,
                    Res.drawable.exclamation,
                    Color.Red
                )
                dialogViewModel.onShowDialogChange(true)
            }
        }
    }
}

private fun createUser(result: AuthResult): User {
    return User(
        id = result.uid.toString(),
        displayName = result.displayName.toString(),
        email = result.email.toString(),
        token = result.token.toString()
    )
}