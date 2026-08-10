package dev.bonygod.gymroutine.workout.data.repository

import dev.bonygod.gymroutine.core.crashlytics.reportFailure
import dev.bonygod.gymroutine.workout.data.datasource.WorkoutSessionRemoteDataSource
import dev.bonygod.gymroutine.workout.domain.model.WorkoutSession
import dev.bonygod.gymroutine.workout.domain.repository.WorkoutSessionRepository

class WorkoutSessionRepositoryImpl(
    private val dataSource: WorkoutSessionRemoteDataSource,
) : WorkoutSessionRepository {

    override suspend fun getSession(userId: String, routineId: String): Result<WorkoutSession?> =
        runCatching { dataSource.getSession(userId, routineId) }
            .reportFailure("WorkoutSessionRepository.getSession", mapOf("routineId" to routineId))

    override suspend fun saveSession(userId: String, session: WorkoutSession): Result<Unit> =
        runCatching { dataSource.saveSession(userId, session) }
            .reportFailure("WorkoutSessionRepository.saveSession", mapOf("routineId" to session.routineId))

    override suspend fun clearSession(userId: String, routineId: String): Result<Unit> =
        runCatching { dataSource.clearSession(userId, routineId) }
            .reportFailure("WorkoutSessionRepository.clearSession", mapOf("routineId" to routineId))
}
