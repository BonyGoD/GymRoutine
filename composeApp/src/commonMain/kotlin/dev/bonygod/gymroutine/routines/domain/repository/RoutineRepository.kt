package dev.bonygod.gymroutine.routines.domain.repository

import dev.bonygod.gymroutine.routines.domain.model.Routine

interface RoutineRepository {
    suspend fun getRoutines(userId: String): Result<List<Routine>>
    suspend fun getRoutineById(userId: String, routineId: String): Result<Routine>
    suspend fun createRoutine(userId: String, routine: Routine): Result<Routine>
    suspend fun updateRoutine(userId: String, routine: Routine): Result<Routine>
    suspend fun deleteRoutine(userId: String, routineId: String): Result<Unit>
}
