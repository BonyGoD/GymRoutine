package dev.bonygod.gymroutine.routines.domain.mapper

import dev.bonygod.gymroutine.routines.data.model.ExerciseDto
import dev.bonygod.gymroutine.routines.data.model.RoutineDto
import dev.bonygod.gymroutine.routines.domain.model.Exercise
import dev.bonygod.gymroutine.routines.domain.model.Routine

fun ExerciseDto.toDomain() = Exercise(
    name = name,
    reps = reps,
    sets = sets,
    weight = weight,
    restSeconds = restSeconds,
    days = days,
)

fun Exercise.toDto() = ExerciseDto(
    name = name,
    reps = reps,
    sets = sets,
    weight = weight,
    restSeconds = restSeconds,
    days = days,
)

fun RoutineDto.toDomain() = Routine(
    id = id,
    name = name,
    exercises = exercises.map { it.toDomain() },
)

fun Routine.toDto() = RoutineDto(
    id = id,
    name = name,
    exercises = exercises.map { it.toDto() },
)
