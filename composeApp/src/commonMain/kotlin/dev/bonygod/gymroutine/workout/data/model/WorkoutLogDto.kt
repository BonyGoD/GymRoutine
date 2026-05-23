package dev.bonygod.gymroutine.workout.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class WorkoutLogDto(
    @Transient val id: String = "",
    val routineId: String = "",
    val routineName: String = "",
    /** ISO-8601 date: "YYYY-MM-DD" */
    val date: String = "",
)
