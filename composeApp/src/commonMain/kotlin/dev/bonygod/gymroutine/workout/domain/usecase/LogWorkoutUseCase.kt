package dev.bonygod.gymroutine.workout.domain.usecase

import dev.bonygod.gymroutine.workout.domain.model.WorkoutLog
import dev.bonygod.gymroutine.workout.domain.repository.WorkoutLogRepository
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

class LogWorkoutUseCase(private val repository: WorkoutLogRepository) {

    suspend operator fun invoke(
        userId: String,
        routineId: String,
        routineName: String,
        completado: Boolean = false,
        recoveredFrom: String? = null,
    ) {
        val today = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
            .toString() // "YYYY-MM-DD"
        val log = WorkoutLog(
            id = "",
            routineId = routineId,
            routineName = routineName,
            date = today,
            completado = completado,
            recoveredFrom = recoveredFrom,
        )
        repository.logWorkout(userId, log)
    }
}
