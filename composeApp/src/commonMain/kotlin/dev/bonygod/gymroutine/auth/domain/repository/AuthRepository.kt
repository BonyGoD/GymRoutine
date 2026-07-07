package dev.bonygod.gymroutine.auth.domain.repository

import dev.bonygod.gymroutine.auth.domain.model.ExternalAuthCredential
import dev.bonygod.gymroutine.auth.domain.model.User

interface AuthRepository {

    suspend fun login(
        email: String,
        password: String,
    ): Result<User>

    suspend fun register(
        email: String,
        password: String,
        name: String,
        age: String,
        weight: String,
        height: String,
    ): Result<User>

    suspend fun loginWithExternalProvider(
        credential: ExternalAuthCredential,
    ): Result<User>

    suspend fun sendPasswordReset(email: String): Result<Unit>

    suspend fun logout(): Result<Unit>

    suspend fun getCurrentUser(): Result<User?>
}
