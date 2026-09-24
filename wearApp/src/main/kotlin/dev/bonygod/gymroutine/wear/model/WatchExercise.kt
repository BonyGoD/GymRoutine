package dev.bonygod.gymroutine.wear.model

import kotlinx.serialization.Serializable

@Serializable
data class WatchExercise(
    val index: Int,
    val name: String,
    val sets: Int,
    val reps: Int,
    val weight: Float,
    val restSeconds: Int,
)
