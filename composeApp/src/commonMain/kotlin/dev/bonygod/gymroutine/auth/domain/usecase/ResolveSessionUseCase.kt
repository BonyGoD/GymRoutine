package dev.bonygod.gymroutine.auth.domain.usecase

import dev.bonygod.gymroutine.auth.domain.model.User
import dev.bonygod.gymroutine.auth.domain.repository.AuthRepository

class ResolveSessionUseCase(private val repository: AuthRepository) {

    suspend operator fun invoke(): Result<User?> {
        val hasActiveSession = repository.hasActiveSession().getOrElse { return Result.failure(it) }
        if (!hasActiveSession) return Result.success(null)

        return repository.getCurrentUser().fold(
            onSuccess = { user ->
                if (user != null) {
                    Result.success(user)
                } else {
                    repository.logout().map { null }
                }
            },
            onFailure = { Result.failure(it) },
        )
    }
}
