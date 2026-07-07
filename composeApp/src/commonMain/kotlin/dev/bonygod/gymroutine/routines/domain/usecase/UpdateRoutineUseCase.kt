package dev.bonygod.gymroutine.routines.domain.usecase

import dev.bonygod.gymroutine.routines.domain.model.Routine
import dev.bonygod.gymroutine.routines.domain.repository.RoutineRepository

class UpdateRoutineUseCase(private val repository: RoutineRepository) {

    suspend operator fun invoke(userId: String, routine: Routine): Result<Routine> = repository.updateRoutine(userId, routine)
}
