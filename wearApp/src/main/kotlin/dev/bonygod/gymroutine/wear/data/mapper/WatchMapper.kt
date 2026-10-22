package dev.bonygod.gymroutine.wear.data.mapper

import dev.bonygod.gymroutine.wear.data.model.WatchExerciseDto
import dev.bonygod.gymroutine.wear.data.model.WatchExerciseResultDto
import dev.bonygod.gymroutine.wear.data.model.WatchRoutineDto
import dev.bonygod.gymroutine.wear.data.model.WatchWorkoutResultDto
import dev.bonygod.gymroutine.wear.model.ActiveWorkout
import dev.bonygod.gymroutine.wear.model.WatchExercise
import dev.bonygod.gymroutine.wear.model.WatchRoutine

private const val WATCH_SCHEMA_VERSION = 1

fun WatchRoutineDto.toModel(): WatchRoutine = WatchRoutine(
    id = id,
    name = name,
    days = days,
    exercises = exercises.map { it.toModel() },
    lastCompletedOn = lastCompletedOn,
)

fun WatchExerciseDto.toModel(): WatchExercise = WatchExercise(
    index = index,
    name = name,
    sets = sets,
    reps = reps,
    weight = weight,
    restSeconds = restSeconds,
)

fun ActiveWorkout.toResultDto(resultId: String, date: String, finishedAt: Long): WatchWorkoutResultDto = WatchWorkoutResultDto(
    schemaVersion = WATCH_SCHEMA_VERSION,
    resultId = resultId,
    routineId = routineId,
    routineName = routineName,
    date = date,
    finishedAt = finishedAt,
    exercises = exercises.map { exercise ->
        WatchExerciseResultDto(
            index = exercise.index,
            name = exercise.name,
            completedSets = setsDone(exercise.index),
            skipped = isSkipped(exercise.index),
            weight = exercise.weight,
            reps = exercise.reps,
        )
    },
)
