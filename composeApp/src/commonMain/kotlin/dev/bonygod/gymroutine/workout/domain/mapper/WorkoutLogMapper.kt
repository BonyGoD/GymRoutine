package dev.bonygod.gymroutine.workout.domain.mapper

import dev.bonygod.gymroutine.workout.data.model.WorkoutLogDto
import dev.bonygod.gymroutine.workout.domain.model.WorkoutLog

fun WorkoutLogDto.toDomain() = WorkoutLog(
    id = id,
    routineId = routineId,
    routineName = routineName,
    date = date,
)
