package dev.bonygod.gymroutine.workout.data.repository

import dev.bonygod.gymroutine.workout.data.datasource.WorkoutLogRemoteDataSource
import dev.bonygod.gymroutine.workout.domain.model.WorkoutLog
import dev.bonygod.gymroutine.workout.domain.repository.WorkoutLogRepository
import kotlinx.coroutines.flow.Flow

class WorkoutLogRepositoryImpl(
    private val dataSource: WorkoutLogRemoteDataSource,
) : WorkoutLogRepository {

    override suspend fun logWorkout(userId: String, log: WorkoutLog) = dataSource.logWorkout(userId, log)

    override fun getLogsFlow(userId: String): Flow<List<WorkoutLog>> = dataSource.getLogsFlow(userId)
}
