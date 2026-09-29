package dev.bonygod.gymroutine.watch.data.mapper

import dev.bonygod.gymroutine.core.utils.normalizeDayToken
import dev.bonygod.gymroutine.routines.domain.model.Exercise
import dev.bonygod.gymroutine.routines.domain.model.Routine
import dev.bonygod.gymroutine.watch.data.model.WatchExerciseDto
import dev.bonygod.gymroutine.watch.data.model.WatchRoutineDto

fun Routine.toWatchDto(lastCompletedOn: String?): WatchRoutineDto = WatchRoutineDto(
    id = id,
    name = name,
    days = days.split(",").mapNotNull { normalizeDayToken(it) },
    exercises = exercises.mapIndexed { index, exercise -> exercise.toWatchDto(index) },
    lastCompletedOn = lastCompletedOn,
)

private fun Exercise.toWatchDto(index: Int): WatchExerciseDto = WatchExerciseDto(
    index = index,
    name = name,
    sets = sets,
    reps = reps,
    weight = weight,
    restSeconds = restSeconds,
)
