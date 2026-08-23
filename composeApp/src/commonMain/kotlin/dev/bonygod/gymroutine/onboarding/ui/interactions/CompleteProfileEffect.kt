package dev.bonygod.gymroutine.onboarding.ui.interactions

sealed class CompleteProfileEffect {
    data class ShowError(
        val message: String,
    ) : CompleteProfileEffect()
}
