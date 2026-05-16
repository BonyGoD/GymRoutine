package dev.bonygod.gymroutine.auth.data.repository

import dev.bonygod.gymroutine.auth.data.datasource.AuthRemoteDataSource
import dev.bonygod.gymroutine.auth.data.mapper.toAuthError
import dev.bonygod.gymroutine.auth.domain.model.ExternalAuthCredential
import dev.bonygod.gymroutine.auth.domain.model.User
import dev.bonygod.gymroutine.auth.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val dataSource: AuthRemoteDataSource,
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<User> = runCatching { dataSource.login(email, password) }
        .mapError()

    override suspend fun register(
        email: String,
        password: String,
        name: String,
        age: String,
        weight: String,
        height: String,
    ): Result<User> = runCatching { dataSource.register(email, password, name, age, weight, height) }
        .mapError()

    override suspend fun loginWithExternalProvider(credential: ExternalAuthCredential): Result<User> = runCatching { dataSource.loginWithExternalProvider(credential) }
        .mapError()

    override suspend fun sendPasswordReset(email: String): Result<Unit> = runCatching { dataSource.sendPasswordReset(email) }
        .mapError()

    override suspend fun logout(): Result<Unit> = runCatching { dataSource.logout() }
        .mapError()

    override suspend fun getCurrentUser(): Result<User?> = runCatching { dataSource.getCurrentUser() }
        .mapError()

    private fun <T> Result<T>.mapError(): Result<T> = recoverCatching { throwable ->
        throw when (throwable) {
            is Exception -> throwable.toAuthError()
            else -> throwable
        }
    }
}
