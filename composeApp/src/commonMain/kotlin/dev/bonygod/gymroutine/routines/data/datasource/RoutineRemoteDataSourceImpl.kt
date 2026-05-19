package dev.bonygod.gymroutine.routines.data.datasource

import dev.bonygod.gymroutine.routines.data.mapper.toMap
import dev.bonygod.gymroutine.routines.data.model.RoutineDto
import dev.bonygod.gymroutine.routines.domain.mapper.toDomain
import dev.bonygod.gymroutine.routines.domain.model.Routine
import dev.gitlive.firebase.firestore.FirebaseFirestore

private const val USERS_COLLECTION = "users"
private const val ROUTINES_COLLECTION = "routines"

class RoutineRemoteDataSourceImpl(
    private val firestore: FirebaseFirestore,
) : RoutineRemoteDataSource {

    // ── Helpers ───────────────────────────────────────────────────────────────

    private fun routinesRef(userId: String) = firestore.collection(USERS_COLLECTION)
        .document(userId)
        .collection(ROUTINES_COLLECTION)

    // ── Operations ────────────────────────────────────────────────────────────

    override suspend fun getRoutines(userId: String): List<Routine> = routinesRef(userId).get().documents.map { doc ->
        doc.data<RoutineDto>().copy(id = doc.reference.id).toDomain()
    }

    override suspend fun getRoutineById(userId: String, routineId: String): Routine {
        val doc = routinesRef(userId).document(routineId).get()
        return doc.data<RoutineDto>().copy(id = doc.reference.id).toDomain()
    }

    override suspend fun createRoutine(userId: String, routine: Routine): Routine {
        val ref = routinesRef(userId).document
        ref.set(routine.toMap())
        return routine.copy(id = ref.id)
    }

    override suspend fun updateRoutine(userId: String, routine: Routine): Routine {
        routinesRef(userId).document(routine.id).set(routine.toMap())
        return routine
    }

    override suspend fun deleteRoutine(userId: String, routineId: String) {
        routinesRef(userId).document(routineId).delete()
    }
}
