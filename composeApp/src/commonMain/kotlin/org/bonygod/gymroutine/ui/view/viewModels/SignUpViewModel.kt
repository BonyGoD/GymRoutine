package org.bonygod.gymroutine.ui.view.viewModels

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.gitlive.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SignUpViewModel: ViewModel() {

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

    fun signUp(auth: FirebaseAuth) {
        viewModelScope.launch {
            try {
                auth.createUserWithEmailAndPassword(email.value, password.value)
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
}