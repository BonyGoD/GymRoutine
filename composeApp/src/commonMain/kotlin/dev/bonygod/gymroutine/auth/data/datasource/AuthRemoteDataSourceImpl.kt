package dev.bonygod.gymroutine.auth.data.datasource

import dev.bonygod.gymroutine.auth.data.mapper.FIELD_AGE
import dev.bonygod.gymroutine.auth.data.mapper.FIELD_HEIGHT
import dev.bonygod.gymroutine.auth.data.mapper.FIELD_NAME
import dev.bonygod.gymroutine.auth.data.mapper.FIELD_WEIGHT
import dev.bonygod.gymroutine.auth.data.mapper.toMap
import dev.bonygod.gymroutine.auth.data.mapper.toUserDto
import dev.bonygod.gymroutine.auth.domain.error.AuthError
import dev.bonygod.gymroutine.auth.domain.mapper.toDomain
import dev.bonygod.gymroutine.auth.domain.mapper.toDto
import dev.bonygod.gymroutine.auth.domain.model.ExternalAuthCredential
import dev.bonygod.gymroutine.auth.domain.model.User
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.firestore.CollectionReference
import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds

class AuthRemoteDataSourceImpl(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
) : AuthRemoteDataSource {

    private val usersCollection get() = firestore.collection(USERS_COLLECTION)

    override suspend fun login(email: String, password: String): User {
        val result = auth.signInWithEmailAndPassword(email, password)
        val uid = result.user?.uid ?: throw AuthError.Unauthorized()
        return fetchUser(uid)
    }

    override suspend fun register(
        email: String,
        password: String,
        name: String,
        age: String,
        weight: String,
        height: String,
    ): User {
        val result = auth.createUserWithEmailAndPassword(email, password)
        val uid = result.user?.uid ?: throw AuthError.Unauthorized()
        val user = User(uid = uid, name = name, age = age, weight = weight, height = height, email = email)
        saveUser(user)
        return user
    }

    override suspend fun loginWithExternalProvider(credential: ExternalAuthCredential): User {
        val existingUser = fetchUserOrNull(credential.uid)
        if (existingUser != null) return existingUser

        val newUser = User(
            uid = credential.uid,
            name = credential.displayName,
            age = "",
            weight = "",
            height = "",
            email = credential.email,
        )
        saveUser(newUser)
        return newUser
    }

    // set(..., merge = true) escribe solo estos tres campos sin sobrescribir el documento
    // entero: un .set(map) plano aquí borraría name/email/uid, que no forman parte del mapa.
    override suspend fun updateUserProfile(
        uid: String,
        age: String,
        weight: String,
        height: String,
    ): User {
        val updates = mapOf(
            FIELD_AGE to age,
            FIELD_WEIGHT to weight,
            FIELD_HEIGHT to height,
        )
        usersCollection.document(uid).set(updates, merge = true)
        return fetchUser(uid)
    }

    override suspend fun updateUserName(uid: String, name: String): User {
        usersCollection.document(uid).set(mapOf(FIELD_NAME to name), merge = true)
        return fetchUser(uid)
    }

    override suspend fun sendPasswordReset(email: String) {
        auth.sendPasswordResetEmail(email)
    }

    override suspend fun logout() {
        auth.signOut()
    }

    override suspend fun getCurrentUser(): User? {
        val uid = auth.currentUser?.uid ?: return null
        return fetchUserOrNull(uid)
    }

    override suspend fun hasActiveSession(): Boolean {
        val restoredUser = withTimeoutOrNull(3.seconds) {
            auth.authStateChanged.first()
        }
        return (restoredUser ?: auth.currentUser) != null
    }

    override suspend fun hasRecentLogin(): Boolean {
        val lastSignInEpochMillis = auth.currentUser?.metaData?.lastSignInEpochMillis() ?: return false
        val elapsedMillis = Clock.System.now().toEpochMilliseconds() - lastSignInEpochMillis
        return elapsedMillis < RECENT_LOGIN_WINDOW_MILLIS
    }

    override suspend fun deleteAccount() {
        val user = auth.currentUser ?: throw AuthError.UserNotFound()
        val uid = user.uid
        val userDocument = usersCollection.document(uid)
        deleteAllDocuments(userDocument.collection(ROUTINES_COLLECTION))
        deleteAllDocuments(userDocument.collection(WORKOUT_LOGS_COLLECTION))
        deleteAllDocuments(userDocument.collection(WORKOUT_SESSIONS_COLLECTION))
        userDocument.delete()
        user.delete()
    }

    private suspend fun deleteAllDocuments(collection: CollectionReference) {
        collection.get().documents.forEach { it.reference.delete() }
    }

    private suspend fun fetchUser(uid: String): User = fetchUserOrNull(uid) ?: throw AuthError.UserNotFound()

    private suspend fun fetchUserOrNull(uid: String): User? {
        val doc = usersCollection.document(uid).get()
        if (!doc.exists) return null
        return doc.toUserDto(fallbackUid = uid).toDomain()
    }

    private suspend fun saveUser(user: User) {
        usersCollection.document(user.uid).set(user.toDto().toMap())
    }

    private companion object {
        const val USERS_COLLECTION = "users"
        const val ROUTINES_COLLECTION = "routines"
        const val WORKOUT_LOGS_COLLECTION = "workoutLogs"
        const val WORKOUT_SESSIONS_COLLECTION = "workoutSessions"
        const val RECENT_LOGIN_WINDOW_MILLIS = 4 * 60 * 1000L
    }
}
