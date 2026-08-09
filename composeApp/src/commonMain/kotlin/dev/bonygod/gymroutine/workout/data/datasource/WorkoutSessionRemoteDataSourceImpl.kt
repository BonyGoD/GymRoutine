package dev.bonygod.gymroutine.workout.data.datasource

import dev.bonygod.gymroutine.workout.data.mapper.toMap
import dev.bonygod.gymroutine.workout.data.model.WorkoutSessionDto
import dev.bonygod.gymroutine.workout.domain.mapper.toDomain
import dev.bonygod.gymroutine.workout.domain.model.WorkoutSession
import dev.gitlive.firebase.firestore.FirebaseFirestore

private const val USERS_COLLECTION = "users"
private const val WORKOUT_SESSIONS_COLLECTION = "workoutSessions"

class WorkoutSessionRemoteDataSourceImpl(
    private val firestore: FirebaseFirestore,
) : WorkoutSessionRemoteDataSource {

    private fun sessionsRef(userId: String) = firestore
        .collection(USERS_COLLECTION)
        .document(userId)
        .collection(WORKOUT_SESSIONS_COLLECTION)

    override suspend fun getSession(userId: String, routineId: String): WorkoutSession? {
        val doc = sessionsRef(userId).document(routineId).get()
        if (!doc.exists) return null
        return doc.data<WorkoutSessionDto>().toDomain()
    }

    override suspend fun saveSession(userId: String, session: WorkoutSession) {
        sessionsRef(userId).document(session.routineId).set(session.toMap())
    }

    override suspend fun clearSession(userId: String, routineId: String) {
        sessionsRef(userId).document(routineId).delete()
    }
}
