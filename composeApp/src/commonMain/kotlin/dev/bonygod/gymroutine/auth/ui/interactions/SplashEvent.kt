package dev.bonygod.gymroutine.auth.ui.interactions

sealed class SplashEvent {
    data object OnRetry : SplashEvent()

    data object OnGoToLogin : SplashEvent()
}
