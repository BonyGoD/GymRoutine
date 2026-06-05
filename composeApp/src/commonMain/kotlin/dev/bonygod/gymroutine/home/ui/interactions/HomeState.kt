package dev.bonygod.gymroutine.home.ui.interactions

import dev.bonygod.gymroutine.routines.domain.model.Routine
import dev.bonygod.gymroutine.workout.domain.model.WorkoutLog

data class HomeState(
    val userName: String = "",
    val routines: List<Routine> = emptyList(),
    val workoutLogs: List<WorkoutLog> = emptyList(),
    val userWeightKg: Float = 70f,
    val todayKcal: Int = 0,
    val consistency: Int = 0,
    val isTodayCompleted: Boolean = false,
    val weekRecordCount: Int = 0,
    val weekRecordSubtitle: String = "",
) {
    fun setUserName(name: String) = copy(userName = name)
    fun setRoutines(routines: List<Routine>) = copy(routines = routines)
    fun setWorkoutLogs(logs: List<WorkoutLog>) = copy(workoutLogs = logs)
    fun setUserWeight(weightKg: Float) = copy(userWeightKg = weightKg)
    fun setTodayKcal(kcal: Int) = copy(todayKcal = kcal)
    fun setConsistency(pct: Int) = copy(consistency = pct)
    fun setIsTodayCompleted(completed: Boolean) = copy(isTodayCompleted = completed)
    fun setWeekRecords(count: Int, subtitle: String) = copy(weekRecordCount = count, weekRecordSubtitle = subtitle)
}
