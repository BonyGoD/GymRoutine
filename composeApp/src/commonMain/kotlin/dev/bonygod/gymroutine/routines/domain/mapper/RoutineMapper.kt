package dev.bonygod.gymroutine.routines.domain.mapper

import dev.bonygod.gymroutine.core.utils.normalizeDayToken
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
)

fun Exercise.toDto() = ExerciseDto(
    name = name,
    reps = reps,
    sets = sets,
    weight = weight,
    restSeconds = restSeconds,
)

fun RoutineDto.toDomain() = Routine(
    id = id,
    name = name,
    days = days.ifBlank {
        exercises
            .flatMap { it.days.split(",").map { d -> d.trim().uppercase() } }
            .filter { it.isNotBlank() }
            .distinct()
            .joinToString(",")
    },
    exercises = exercises.map { it.toDomain() },
)

fun List<Routine>.hasRoutineForDay(dayAbbr: String): Boolean = any { routine ->
    routine.days.split(",").any { part ->
        normalizeDayToken(part) == dayAbbr
    }
}

fun List<Routine>.routinesForDay(dayAbbr: String): List<Routine> = filter { routine ->
    routine.days.split(",").any { part ->
        normalizeDayToken(part) == dayAbbr
    }
}
