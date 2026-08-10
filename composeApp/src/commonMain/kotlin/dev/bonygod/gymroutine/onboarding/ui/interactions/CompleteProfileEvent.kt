package dev.bonygod.gymroutine.onboarding.ui.interactions

sealed class CompleteProfileEvent {
    data class OnInit(
        val userId: String,
    ) : CompleteProfileEvent()

    data class OnAgeChange(
        val value: Int,
    ) : CompleteProfileEvent()

    data class OnHeightChange(
        val value: Int,
    ) : CompleteProfileEvent()

    data class OnWeightChange(
        val value: Int,
    ) : CompleteProfileEvent()

    data object OnSaveClick : CompleteProfileEvent()
}
