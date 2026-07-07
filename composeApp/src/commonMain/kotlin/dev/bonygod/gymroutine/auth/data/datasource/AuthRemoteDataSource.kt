package dev.bonygod.gymroutine.auth.data.datasource

import dev.bonygod.gymroutine.auth.domain.model.ExternalAuthCredential
import dev.bonygod.gymroutine.auth.domain.model.User

interface AuthRemoteDataSource {

    suspend fun login(email: String, password: String): User

    suspend fun register(
        email: String,
        password: String,
        name: String,
        age: String,
        weight: String,
        height: String,
    ): User

    suspend fun loginWithExternalProvider(credential: ExternalAuthCredential): User

    suspend fun sendPasswordReset(email: String)

    suspend fun logout()

    suspend fun getCurrentUser(): User?
}
