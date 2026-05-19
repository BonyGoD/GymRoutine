package dev.bonygod.gymroutine.routines.data.datasource

import dev.bonygod.gymroutine.routines.domain.model.Routine

interface RoutineRemoteDataSource {
    suspend fun getRoutines(userId: String): List<Routine>
    suspend fun getRoutineById(userId: String, routineId: String): Routine
    suspend fun createRoutine(userId: String, routine: Routine): Routine
    suspend fun updateRoutine(userId: String, routine: Routine): Routine
    suspend fun deleteRoutine(userId: String, routineId: String)
}
