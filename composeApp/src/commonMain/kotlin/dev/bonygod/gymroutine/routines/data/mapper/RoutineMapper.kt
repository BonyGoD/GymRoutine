package dev.bonygod.gymroutine.routines.data.mapper

import dev.bonygod.gymroutine.routines.domain.model.Exercise
import dev.bonygod.gymroutine.routines.domain.model.ExerciseProgress
import dev.bonygod.gymroutine.routines.domain.model.Routine

internal fun ExerciseProgress.toMap(): Map<String, Any> = mapOf(
    "weight" to weight,
    "reps" to reps,
    "timestamp" to timestamp,
)

internal fun Exercise.toMap(): Map<String, Any> = mapOf(
    "name" to name,
    "sets" to sets,
    "restSeconds" to restSeconds,
    "history" to history.map { it.toMap() },
    // Desnormalizados: los lee cualquier build antigua de la app y facilitan queries en Firestore.
    "reps" to reps,
    "weight" to weight,
    "initialWeight" to initialWeight,
    "initialReps" to initialReps,
)

internal fun Routine.toMap(): Map<String, Any> = mapOf(
    "name" to name,
    "days" to days,
    "exercises" to exercises.map { it.toMap() },
)
