package dev.bonygod.gymroutine.watch.data.model

import kotlinx.serialization.Serializable

@Serializable
data class WatchRoutinesPayloadDto(
    val schemaVersion: Int,
    val generatedAt: Long,
    val routines: List<WatchRoutineDto>,
)

@Serializable
data class WatchRoutineDto(
    val id: String,
    val name: String,
    val days: List<String>,
    val exercises: List<WatchExerciseDto>,
    val lastCompletedOn: String? = null,
)

@Serializable
data class WatchExerciseDto(
    val index: Int,
    val name: String,
    val sets: Int,
    val reps: Int,
    val weight: Float,
    val restSeconds: Int,
)
