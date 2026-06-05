package dev.bonygod.gymroutine.routines.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ExerciseDto(
    val name: String = "",
    val reps: Int = 0,
    val sets: Int = 0,
    val weight: Float = 0f,
    val restSeconds: Int = 0,
    val days: String = "",
    val initialWeight: Float = 0f,
    val initialReps: Int = 0,
)
