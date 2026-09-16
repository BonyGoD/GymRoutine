package dev.bonygod.gymroutine.auth.ui.interactions

data class SplashState(
    val isLoading: Boolean = true,
) {
    fun showLoading() = copy(isLoading = true)

    fun showError() = copy(isLoading = false)
}
