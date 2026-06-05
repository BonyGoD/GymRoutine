package dev.bonygod.gymroutine.workout.data.datasource

import dev.bonygod.gymroutine.workout.data.mapper.toMap
import dev.bonygod.gymroutine.workout.data.model.WorkoutLogDto
import dev.bonygod.gymroutine.workout.domain.mapper.toDomain
import dev.bonygod.gymroutine.workout.domain.model.WorkoutLog
import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val USERS_COLLECTION = "users"
private const val WORKOUT_LOGS_COLLECTION = "workoutLogs"

class WorkoutLogRemoteDataSourceImpl(
    private val firestore: FirebaseFirestore,
) : WorkoutLogRemoteDataSource {

    private fun logsRef(userId: String) = firestore
        .collection(USERS_COLLECTION)
        .document(userId)
        .collection(WORKOUT_LOGS_COLLECTION)

    override suspend fun logWorkout(userId: String, log: WorkoutLog) {
        logsRef(userId).document.set(log.toMap())
    }

    override fun getLogsFlow(userId: String): Flow<List<WorkoutLog>> = logsRef(userId).snapshots.map { snapshot ->
        snapshot.documents.map { doc ->
            doc.data<WorkoutLogDto>().copy(id = doc.reference.id).toDomain()
        }
    }
}
