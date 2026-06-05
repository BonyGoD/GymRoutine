package dev.bonygod.gymroutine.routines.data.mapper

import dev.bonygod.gymroutine.routines.domain.model.Exercise
import dev.bonygod.gymroutine.routines.domain.model.Routine

internal fun Exercise.toMap(): Map<String, Any> = mapOf(
    "name" to name,
    "reps" to reps,
    "sets" to sets,
    "weight" to weight,
    "restSeconds" to restSeconds,
    "initialWeight" to initialWeight,
    "initialReps" to initialReps,
)

internal fun Routine.toMap(): Map<String, Any> = mapOf(
    "name" to name,
    "days" to days,
    "exercises" to exercises.map { it.toMap() },
)
