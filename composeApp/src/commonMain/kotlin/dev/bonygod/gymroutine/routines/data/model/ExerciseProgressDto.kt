package dev.bonygod.gymroutine.routines.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ExerciseProgressDto(
    val weight: Float = 0f,
    val reps: Int = 0,
    val timestamp: Long = 0L,
)
