package dev.bonygod.gymroutine.workout.domain.usecase

import dev.bonygod.gymroutine.workout.domain.model.WorkoutSession
import dev.bonygod.gymroutine.workout.domain.repository.WorkoutSessionRepository

class SaveWorkoutSessionUseCase(private val repository: WorkoutSessionRepository) {

    suspend operator fun invoke(userId: String, session: WorkoutSession): Result<Unit> =
        repository.saveSession(userId, session)
}
