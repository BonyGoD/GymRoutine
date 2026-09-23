package dev.bonygod.gymroutine.profile.ui.interactions

sealed class ProfileEvent {
    data object OnLogout : ProfileEvent()
    data object OnEditProfileData : ProfileEvent()
    data object OnDismissEditProfileData : ProfileEvent()
    data class OnAgeChange(val value: Int) : ProfileEvent()
    data class OnHeightChange(val value: Int) : ProfileEvent()
    data class OnWeightChange(val value: Int) : ProfileEvent()
    data object OnSaveProfileData : ProfileEvent()
    data object OnEditName : ProfileEvent()
    data object OnDismissEditName : ProfileEvent()
    data class OnNameChange(val value: String) : ProfileEvent()
    data object OnSaveName : ProfileEvent()
    data object OnDeleteAccountClick : ProfileEvent()
    data object OnDismissDeleteAccount : ProfileEvent()
    data object OnConfirmDeleteAccount : ProfileEvent()
    data object OnDismissRelogin : ProfileEvent()
    data object OnReloginClick : ProfileEvent()
}
