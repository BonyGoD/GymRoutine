package dev.bonygod.gymroutine.watch.data.model

import kotlinx.serialization.Serializable

@Serializable
data class WatchWorkoutResultDto(
    val schemaVersion: Int,
    val resultId: String,
    val routineId: String,
    val routineName: String,
    val date: String,
    val finishedAt: Long,
    val exercises: List<WatchExerciseResultDto>,
)

@Serializable
data class WatchExerciseResultDto(
    val index: Int,
    val name: String,
    val completedSets: Int,
    val skipped: Boolean,
    val weight: Float,
    val reps: Int,
)
