package dev.bonygod.gymroutine.auth.ui.interactions

data class AuthUI(
    val userName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
)

data class AuthState(
    val authUI: AuthUI = AuthUI(),
    val eyePasswordOpen: Boolean = false,
    val eyeConfirmPassword: Boolean = false,
    val isLoading: Boolean = false,
    val showDialog: Boolean = false,
) {
    fun showDialog(show: Boolean) = copy(showDialog = show)

    fun showLoading(show: Boolean) = copy(isLoading = show)

    fun updateUserName(value: String) = copy(authUI = authUI.copy(userName = value))

    fun updateEmail(value: String) = copy(authUI = authUI.copy(email = value))

    fun updatePassword(value: String) = copy(authUI = authUI.copy(password = value))

    fun updateConfirmPassword(value: String) = copy(authUI = authUI.copy(confirmPassword = value))

    fun updateEyePassword() = copy(eyePasswordOpen = !eyePasswordOpen)

    fun updateEyeConfirmPassword() = copy(eyeConfirmPassword = !eyeConfirmPassword)

    fun getUserData() = authUI

    fun setUserData(authUI: AuthUI) = copy(authUI = authUI)

    fun isValidEmail(): Boolean = authUI.email.contains('@') && authUI.email.contains('.')

    fun isNameFilled(): Boolean = authUI.userName.isNotBlank()

    fun passwordsMatch(): Boolean = authUI.password == authUI.confirmPassword

    fun isPasswordLongEnough(): Boolean = authUI.password.length >= MIN_PASSWORD_LENGTH

    companion object {
        const val MIN_PASSWORD_LENGTH = 6
    }
}
