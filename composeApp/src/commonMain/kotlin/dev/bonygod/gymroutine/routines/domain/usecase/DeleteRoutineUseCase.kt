package dev.bonygod.gymroutine.routines.domain.usecase

import dev.bonygod.gymroutine.routines.domain.repository.RoutineRepository

class DeleteRoutineUseCase(private val repository: RoutineRepository) {

    suspend operator fun invoke(userId: String, routineId: String): Result<Unit> = repository.deleteRoutine(userId, routineId)
}
