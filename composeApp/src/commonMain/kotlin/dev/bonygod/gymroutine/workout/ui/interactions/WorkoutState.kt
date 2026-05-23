package dev.bonygod.gymroutine.workout.ui.interactions

data class WorkoutState(
    val isStarted: Boolean = false,
    val isLogging: Boolean = false,
) {
    fun start() = copy(isStarted = true)
    fun stop() = copy(isStarted = false, isLogging = false)
    fun showLogging() = copy(isLogging = true)
}
