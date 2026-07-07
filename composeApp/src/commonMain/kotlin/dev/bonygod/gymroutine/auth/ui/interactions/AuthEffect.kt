package dev.bonygod.gymroutine.auth.ui.interactions

sealed class AuthEffect {
    data class ShowError(
        val message: String,
    ) : AuthEffect()

    data object DismissDialog : AuthEffect()

    data class NavigateToMain(
        val userId: String,
    ) : AuthEffect()
}
