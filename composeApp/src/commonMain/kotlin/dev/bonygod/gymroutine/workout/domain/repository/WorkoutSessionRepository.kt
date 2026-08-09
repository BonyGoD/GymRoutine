package dev.bonygod.gymroutine.workout.domain.repository

import dev.bonygod.gymroutine.workout.domain.model.WorkoutSession

interface WorkoutSessionRepository {
    suspend fun getSession(userId: String, routineId: String): Result<WorkoutSession?>
    suspend fun saveSession(userId: String, session: WorkoutSession): Result<Unit>
    suspend fun clearSession(userId: String, routineId: String): Result<Unit>
}
