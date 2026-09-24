package dev.bonygod.gymroutine.watch.domain.model

data class WatchWorkoutResult(
    val resultId: String,
    val routineId: String,
    val routineName: String,
    val date: String,
    val finishedAt: Long,
    val exercises: List<WatchExerciseResult>,
)

data class WatchExerciseResult(
    val index: Int,
    val name: String,
    val completedSets: Int,
    val skipped: Boolean,
    val weight: Float,
    val reps: Int,
)
