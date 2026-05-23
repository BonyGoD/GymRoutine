package dev.bonygod.gymroutine.workout.domain.usecase

import dev.bonygod.gymroutine.workout.domain.model.WorkoutLog
import dev.bonygod.gymroutine.workout.domain.repository.WorkoutLogRepository
import kotlinx.coroutines.flow.Flow

class ObserveWorkoutLogsUseCase(private val repository: WorkoutLogRepository) {

    operator fun invoke(userId: String): Flow<List<WorkoutLog>> =
        repository.getLogsFlow(userId)
}
