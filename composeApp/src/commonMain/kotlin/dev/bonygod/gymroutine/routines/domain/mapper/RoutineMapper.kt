package dev.bonygod.gymroutine.routines.domain.mapper

import dev.bonygod.gymroutine.core.utils.normalizeDayToken
import dev.bonygod.gymroutine.routines.data.model.ExerciseDto
import dev.bonygod.gymroutine.routines.data.model.ExerciseProgressDto
import dev.bonygod.gymroutine.routines.data.model.RoutineDto
import dev.bonygod.gymroutine.routines.domain.model.Exercise
import dev.bonygod.gymroutine.routines.domain.model.ExerciseProgress
import dev.bonygod.gymroutine.routines.domain.model.Routine

fun ExerciseProgressDto.toDomain() = ExerciseProgress(weight, reps, timestamp)

fun ExerciseDto.toDomain(): Exercise {
    // Migración: los documentos anteriores a `history` traen solo los escalares.
    val progression = if (history.isNotEmpty()) {
        history.map { it.toDomain() }
    } else {
        val firstWeight = if (initialWeight == 0f) weight else initialWeight
        val firstReps = if (initialReps == 0) reps else initialReps
        buildList {
            add(ExerciseProgress(firstWeight, firstReps, 0L))
            if (firstWeight != weight || firstReps != reps) {
                add(ExerciseProgress(weight, reps, 0L))
            }
        }
    }
    return Exercise(name = name, sets = sets, restSeconds = restSeconds, history = progression)
}

fun Exercise.toDto() = ExerciseDto(
    name = name,
    reps = reps,
    sets = sets,
    weight = weight,
    restSeconds = restSeconds,
    initialWeight = initialWeight,
    initialReps = initialReps,
    history = history.map { ExerciseProgressDto(it.weight, it.reps, it.timestamp) },
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

fun List<Routine>.hasRoutineForDay(dayAbbr: String): Boolean {
    val normalizedTarget = normalizeDayToken(dayAbbr) ?: return false
    return any { routine ->
        routine.days.split(",").any { part ->
            normalizeDayToken(part) == normalizedTarget
        }
    }
}

fun List<Routine>.routinesForDay(dayAbbr: String): List<Routine> {
    val normalizedTarget = normalizeDayToken(dayAbbr) ?: return emptyList()
    return filter { routine ->
        routine.days.split(",").any { part ->
            normalizeDayToken(part) == normalizedTarget
        }
    }
}
