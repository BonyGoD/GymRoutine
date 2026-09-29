package dev.bonygod.gymroutine.wear.model

import kotlinx.serialization.Serializable

@Serializable
data class ActiveWorkout(
    val routineId: String,
    val routineName: String,
    val exercises: List<WatchExercise>,
    val completedSets: Map<Int, Int> = emptyMap(),
    val skipped: Set<Int> = emptySet(),
    val startedOn: String,
) {
    fun setsDone(index: Int): Int = completedSets[index] ?: 0

    fun isExerciseDone(index: Int): Boolean {
        val exercise = exercises.getOrNull(index) ?: return false
        return exercise.sets > 0 && setsDone(index) >= exercise.sets
    }

    fun isSkipped(index: Int): Boolean = index in skipped

    fun completeSet(index: Int): ActiveWorkout {
        val exercise = exercises.getOrNull(index) ?: return this
        val done = (setsDone(index) + 1).coerceAtMost(exercise.sets)
        return copy(completedSets = completedSets + (index to done))
    }

    fun toggleSkip(index: Int): ActiveWorkout = if (index in skipped) {
        copy(skipped = skipped - index)
    } else {
        copy(skipped = skipped + index)
    }

    fun doneCount(): Int = exercises.indices.count { isExerciseDone(it) || isSkipped(it) }

    fun hasProgress(): Boolean = completedSets.values.any { it > 0 }
}
