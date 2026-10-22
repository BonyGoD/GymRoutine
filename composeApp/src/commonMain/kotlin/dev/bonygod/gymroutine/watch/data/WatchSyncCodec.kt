package dev.bonygod.gymroutine.watch.data

import dev.bonygod.gymroutine.routines.domain.model.Routine
import dev.bonygod.gymroutine.watch.data.mapper.toWatchDto
import dev.bonygod.gymroutine.watch.data.model.WatchRoutinesPayloadDto
import dev.bonygod.gymroutine.watch.data.model.WatchWorkoutResultDto
import dev.bonygod.gymroutine.watch.domain.mapper.toDomain
import dev.bonygod.gymroutine.watch.domain.model.WatchWorkoutResult
import dev.bonygod.gymroutine.workout.domain.model.WorkoutLog
import kotlinx.serialization.json.Json

const val WATCH_SCHEMA_VERSION = 1

object WatchSyncCodec {

    private val format = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    fun encodeRoutines(routines: List<Routine>, logs: List<WorkoutLog>, generatedAt: Long): String {
        val payload = WatchRoutinesPayloadDto(
            schemaVersion = WATCH_SCHEMA_VERSION,
            generatedAt = generatedAt,
            routines = routines.map { routine ->
                val lastCompletedOn = logs
                    .filter { it.routineId == routine.id && it.completado && it.recoveredFrom == null }
                    .maxOfOrNull { it.date }
                routine.toWatchDto(lastCompletedOn)
            },
        )
        return format.encodeToString(payload)
    }

    fun decodeResult(json: String): WatchWorkoutResult? = runCatching {
        format.decodeFromString<WatchWorkoutResultDto>(json)
    }.getOrNull()
        ?.takeIf { it.schemaVersion == WATCH_SCHEMA_VERSION }
        ?.toDomain()
}
