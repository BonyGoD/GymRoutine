package dev.bonygod.gymroutine.workout.domain.repository

import dev.bonygod.gymroutine.workout.domain.model.WorkoutLog
import kotlinx.coroutines.flow.Flow

interface WorkoutLogRepository {
    suspend fun logWorkout(userId: String, log: WorkoutLog)
    fun getLogsFlow(userId: String): Flow<List<WorkoutLog>>
}
