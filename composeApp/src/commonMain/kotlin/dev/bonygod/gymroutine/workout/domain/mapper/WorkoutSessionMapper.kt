package dev.bonygod.gymroutine.workout.domain.mapper

import dev.bonygod.gymroutine.workout.data.model.ExerciseSessionProgressDto
import dev.bonygod.gymroutine.workout.data.model.WorkoutSessionDto
import dev.bonygod.gymroutine.workout.domain.model.ExerciseSessionProgress
import dev.bonygod.gymroutine.workout.domain.model.WorkoutSession

fun ExerciseSessionProgressDto.toDomain() = ExerciseSessionProgress(
    index = index,
    completedSets = completedSets,
    isCompleted = isCompleted,
    isSkipped = isSkipped,
)

fun WorkoutSessionDto.toDomain() = WorkoutSession(
    routineId = routineId,
    date = date,
    exerciseCount = exerciseCount,
    exercises = exercises.map { it.toDomain() },
)
