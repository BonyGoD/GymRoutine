package dev.bonygod.gymroutine.watch.domain.mapper

import dev.bonygod.gymroutine.watch.data.model.WatchExerciseResultDto
import dev.bonygod.gymroutine.watch.data.model.WatchWorkoutResultDto
import dev.bonygod.gymroutine.watch.domain.model.WatchExerciseResult
import dev.bonygod.gymroutine.watch.domain.model.WatchWorkoutResult

fun WatchWorkoutResultDto.toDomain(): WatchWorkoutResult = WatchWorkoutResult(
    resultId = resultId,
    routineId = routineId,
    routineName = routineName,
    date = date,
    finishedAt = finishedAt,
    exercises = exercises.map { it.toDomain() },
)

private fun WatchExerciseResultDto.toDomain(): WatchExerciseResult = WatchExerciseResult(
    index = index,
    name = name,
    completedSets = completedSets,
    skipped = skipped,
    weight = weight,
    reps = reps,
)
