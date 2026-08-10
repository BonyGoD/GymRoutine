package dev.bonygod.gymroutine.auth.data.repository

import dev.bonygod.crashlytics.kmp.core.CrashlyticsKMP
import dev.bonygod.gymroutine.auth.data.datasource.AuthRemoteDataSource
import dev.bonygod.gymroutine.auth.data.mapper.toAuthError
import dev.bonygod.gymroutine.auth.domain.model.ExternalAuthCredential
import dev.bonygod.gymroutine.auth.domain.model.User
import dev.bonygod.gymroutine.auth.domain.repository.AuthRepository
import dev.bonygod.gymroutine.core.crashlytics.reportFailure

class AuthRepositoryImpl(
    private val dataSource: AuthRemoteDataSource,
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<User> =
        runCatching { dataSource.login(email, password) }
            .reportFailure("AuthRepository.login")
            .mapError()
            .identifyUser()

    override suspend fun register(
        email: String,
        password: String,
        name: String,
        age: String,
        weight: String,
        height: String,
    ): Result<User> =
        runCatching { dataSource.register(email, password, name, age, weight, height) }
            .reportFailure("AuthRepository.register")
            .mapError()
            .identifyUser()

    override suspend fun loginWithExternalProvider(credential: ExternalAuthCredential): Result<User> =
        runCatching { dataSource.loginWithExternalProvider(credential) }
            .reportFailure("AuthRepository.loginWithExternalProvider")
            .mapError()
            .identifyUser()

    override suspend fun sendPasswordReset(email: String): Result<Unit> =
        runCatching { dataSource.sendPasswordReset(email) }
            .reportFailure("AuthRepository.sendPasswordReset")
            .mapError()

    override suspend fun logout(): Result<Unit> =
        runCatching { dataSource.logout() }
            .reportFailure("AuthRepository.logout")
            .mapError()
            .onSuccess { CrashlyticsKMP.reporter.setUserId(null) }

    override suspend fun getCurrentUser(): Result<User?> =
        runCatching { dataSource.getCurrentUser() }
            .reportFailure("AuthRepository.getCurrentUser")
            .mapError()
            .identifyUser()

    private fun <T> Result<T>.mapError(): Result<T> = recoverCatching { throwable ->
        throw when (throwable) {
            is Exception -> throwable.toAuthError()
            else -> throwable
        }
    }

    /** Identifica al usuario en Crashlytics en cuanto la sesión queda resuelta, sin tocar el [Result]. */
    private fun <T : User?> Result<T>.identifyUser(): Result<T> = onSuccess { user ->
        user?.let { CrashlyticsKMP.reporter.setUserId(it.uid) }
    }
}
