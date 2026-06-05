package dev.bonygod.gymroutine.profile.ui.interactions

sealed class ProfileEvent {
    data object OnLogout : ProfileEvent()
}
