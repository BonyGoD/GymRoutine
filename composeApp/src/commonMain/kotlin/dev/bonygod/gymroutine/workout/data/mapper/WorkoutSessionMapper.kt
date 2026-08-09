package dev.bonygod.gymroutine.workout.data.mapper

import dev.bonygod.gymroutine.workout.domain.model.ExerciseSessionProgress
import dev.bonygod.gymroutine.workout.domain.model.WorkoutSession

internal fun ExerciseSessionProgress.toMap(): Map<String, Any> = mapOf(
    "index" to index,
    "completedSets" to completedSets,
    "isCompleted" to isCompleted,
    "isSkipped" to isSkipped,
)

internal fun WorkoutSession.toMap(): Map<String, Any> = mapOf(
    "routineId" to routineId,
    "date" to date,
    "exerciseCount" to exerciseCount,
    "exercises" to exercises.map { it.toMap() },
)
