package dev.bonygod.gymroutine.wear.ui.interactions

sealed class WatchEffect {
    data class ExerciseCompleted(val index: Int) : WatchEffect()
    data object WorkoutSaved : WatchEffect()
    data object SaveFailed : WatchEffect()
}
