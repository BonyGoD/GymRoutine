package dev.bonygod.gymroutine.workout.data.datasource

import dev.bonygod.gymroutine.workout.domain.model.WorkoutSession

interface WorkoutSessionRemoteDataSource {
    suspend fun getSession(userId: String, routineId: String): WorkoutSession?
    suspend fun saveSession(userId: String, session: WorkoutSession)
    suspend fun clearSession(userId: String, routineId: String)
}
