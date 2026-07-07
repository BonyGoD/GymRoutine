package dev.bonygod.gymroutine.routines.domain.usecase

import dev.bonygod.gymroutine.routines.domain.model.Routine
import dev.bonygod.gymroutine.routines.domain.repository.RoutineRepository

class GetRoutinesUseCase(private val repository: RoutineRepository) {

    suspend operator fun invoke(userId: String): Result<List<Routine>> = repository.getRoutines(userId)
}
