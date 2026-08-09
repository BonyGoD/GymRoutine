package dev.bonygod.gymroutine.workout.domain.usecase

import dev.bonygod.gymroutine.workout.domain.model.WorkoutSession
import dev.bonygod.gymroutine.workout.domain.repository.WorkoutSessionRepository

class GetWorkoutSessionUseCase(private val repository: WorkoutSessionRepository) {

    suspend operator fun invoke(userId: String, routineId: String): Result<WorkoutSession?> =
        repository.getSession(userId, routineId)
}
