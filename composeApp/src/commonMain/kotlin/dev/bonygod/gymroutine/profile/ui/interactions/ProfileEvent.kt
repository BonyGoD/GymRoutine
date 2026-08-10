package dev.bonygod.gymroutine.profile.ui.interactions

sealed class ProfileEvent {
    data object OnLogout : ProfileEvent()
    data object OnEditProfileData : ProfileEvent()
    data object OnDismissEditProfileData : ProfileEvent()
    data class OnAgeChange(val value: Int) : ProfileEvent()
    data class OnHeightChange(val value: Int) : ProfileEvent()
    data class OnWeightChange(val value: Int) : ProfileEvent()
    data object OnSaveProfileData : ProfileEvent()
}
