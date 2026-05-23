package dev.bonygod.gymroutine.workout.domain.model

data class WorkoutLog(
    val id: String,
    val routineId: String,
    val routineName: String,
    /** ISO-8601 date: "YYYY-MM-DD" */
    val date: String,
)
