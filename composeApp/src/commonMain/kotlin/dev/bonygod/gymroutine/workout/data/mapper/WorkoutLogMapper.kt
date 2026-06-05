package dev.bonygod.gymroutine.workout.data.mapper

import dev.bonygod.gymroutine.workout.domain.model.WorkoutLog

internal fun WorkoutLog.toMap(): Map<String, Any> = mapOf(
    "routineId" to routineId,
    "routineName" to routineName,
    "date" to date,
    "completado" to completado,
)
