package dev.bonygod.gymroutine.routines.data.repository

import dev.bonygod.gymroutine.routines.data.datasource.RoutineRemoteDataSource
import dev.bonygod.gymroutine.routines.data.mapper.mapError
import dev.bonygod.gymroutine.routines.domain.model.Routine
import dev.bonygod.gymroutine.routines.domain.repository.RoutineRepository
import kotlinx.coroutines.flow.Flow

class RoutineRepositoryImpl(
    private val dataSource: RoutineRemoteDataSource,
) : RoutineRepository {

    override suspend fun getRoutines(userId: String): Result<List<Routine>> = runCatching { dataSource.getRoutines(userId) }.mapError()

    override fun getRoutinesFlow(userId: String): Flow<List<Routine>> = dataSource.getRoutinesFlow(userId)

    override suspend fun getRoutineById(userId: String, routineId: String): Result<Routine> = runCatching { dataSource.getRoutineById(userId, routineId) }.mapError()

    override suspend fun createRoutine(userId: String, routine: Routine): Result<Routine> = runCatching { dataSource.createRoutine(userId, routine) }.mapError()

    override suspend fun updateRoutine(userId: String, routine: Routine): Result<Routine> = runCatching { dataSource.updateRoutine(userId, routine) }.mapError()

    override suspend fun deleteRoutine(userId: String, routineId: String): Result<Unit> = runCatching { dataSource.deleteRoutine(userId, routineId) }.mapError()
}
