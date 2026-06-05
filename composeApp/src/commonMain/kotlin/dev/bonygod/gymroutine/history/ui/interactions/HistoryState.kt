package dev.bonygod.gymroutine.history.ui.interactions

import dev.bonygod.gymroutine.workout.domain.model.WorkoutLog

data class HistoryState(
    val isLoading: Boolean = true,
    val logs: List<WorkoutLog> = emptyList(),
) {
    fun setLoading() = copy(isLoading = true)
    fun setLogs(logs: List<WorkoutLog>) = copy(isLoading = false, logs = logs)
}
