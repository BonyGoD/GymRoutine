package dev.bonygod.gymroutine.profile.ui.interactions

sealed class ProfileEffect {
    data class ShowError(val message: String) : ProfileEffect()
}
