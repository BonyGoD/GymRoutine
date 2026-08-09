package dev.bonygod.gymroutine.workout.data.model

import kotlinx.serialization.Serializable

@Serializable
data class WorkoutSessionDto(
    val routineId: String = "",
    val date: String = "",
    val exerciseCount: Int = 0,
    val exercises: List<ExerciseSessionProgressDto> = emptyList(),
)

@Serializable
data class ExerciseSessionProgressDto(
    val index: Int = 0,
    val completedSets: Int = 0,
    val isCompleted: Boolean = false,
    val isSkipped: Boolean = false,
)
