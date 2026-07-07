package dev.bonygod.gymroutine.workout.data.datasource

import dev.bonygod.gymroutine.workout.domain.model.WorkoutLog
import kotlinx.coroutines.flow.Flow

interface WorkoutLogRemoteDataSource {
    suspend fun logWorkout(userId: String, log: WorkoutLog)
    fun getLogsFlow(userId: String): Flow<List<WorkoutLog>>
}
