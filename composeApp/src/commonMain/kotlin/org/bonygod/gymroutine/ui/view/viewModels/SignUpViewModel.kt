package org.bonygod.gymroutine.ui.view.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel

class SignUpViewModel: ViewModel() {
    var email by mutableStateOf("")
    var user by mutableStateOf("")
    var passwordVisible by mutableStateOf(false)
    var password by mutableStateOf("")
    var passwordVisibleRepeat by mutableStateOf(false)
    var passwordRepeat by mutableStateOf("")
    var colorFirstTextField by mutableStateOf(Color.Black)
    var colorSecondTextField by mutableStateOf(Color.Black)

    fun onPasswordChange(newPassword: String) {
        password = newPassword
        updateTextFieldColors()
    }

    fun onPasswordRepeatChange(newPasswordRepeat: String) {
        passwordRepeat = newPasswordRepeat
        updateTextFieldColors()
    }

    private fun updateTextFieldColors() {
        if (password != passwordRepeat) {
            colorFirstTextField = Color.Red
            colorSecondTextField = Color.Red
        } else {
            colorFirstTextField = Color.Black
            colorSecondTextField = Color.Black
        }
    }
}