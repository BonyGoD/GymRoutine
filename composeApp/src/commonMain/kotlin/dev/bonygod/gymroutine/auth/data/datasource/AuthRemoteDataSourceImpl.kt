package dev.bonygod.gymroutine.auth.data.datasource

import dev.bonygod.gymroutine.auth.data.mapper.toMap
import dev.bonygod.gymroutine.auth.data.mapper.toUserDto
import dev.bonygod.gymroutine.auth.domain.error.AuthError
import dev.bonygod.gymroutine.auth.domain.mapper.toDomain
import dev.bonygod.gymroutine.auth.domain.mapper.toDto
import dev.bonygod.gymroutine.auth.domain.model.ExternalAuthCredential
import dev.bonygod.gymroutine.auth.domain.model.User
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.firestore.FirebaseFirestore

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
    }
}
