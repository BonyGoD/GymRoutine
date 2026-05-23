package dev.bonygod.gymroutine.workout.ui.interactions

sealed class WorkoutEffect {
    data class ShowError(val message: String) : WorkoutEffect()
}
