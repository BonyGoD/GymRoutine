package dev.bonygod.gymroutine.home.ui.interactions

sealed class HomeEffect {
    data class ShowError(val message: String) : HomeEffect()
}
