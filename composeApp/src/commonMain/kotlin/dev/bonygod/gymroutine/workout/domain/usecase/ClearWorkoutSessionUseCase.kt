package dev.bonygod.gymroutine.workout.domain.usecase

import dev.bonygod.gymroutine.workout.domain.repository.WorkoutSessionRepository

class ClearWorkoutSessionUseCase(private val repository: WorkoutSessionRepository) {

    suspend operator fun invoke(userId: String, routineId: String): Result<Unit> =
        repository.clearSession(userId, routineId)
}
