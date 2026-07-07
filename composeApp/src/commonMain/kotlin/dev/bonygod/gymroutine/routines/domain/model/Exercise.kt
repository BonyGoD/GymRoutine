package dev.bonygod.gymroutine.routines.domain.model

data class Exercise(
    val name: String,
    val reps: Int,
    val sets: Int,
    val weight: Float,
    val restSeconds: Int,
    val initialWeight: Float = weight,
    val initialReps: Int = reps,
)
