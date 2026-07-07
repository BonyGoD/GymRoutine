package dev.bonygod.gymroutine.routines.domain.usecase

import dev.bonygod.gymroutine.routines.domain.model.Routine
import dev.bonygod.gymroutine.routines.domain.repository.RoutineRepository
import kotlinx.coroutines.flow.Flow

class ObserveRoutinesUseCase(private val repository: RoutineRepository) {

    operator fun invoke(userId: String): Flow<List<Routine>> = repository.getRoutinesFlow(userId)
}
