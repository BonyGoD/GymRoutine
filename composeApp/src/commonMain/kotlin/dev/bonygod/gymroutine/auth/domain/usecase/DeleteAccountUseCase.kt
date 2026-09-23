package dev.bonygod.gymroutine.auth.domain.usecase

import dev.bonygod.gymroutine.auth.domain.error.AuthError
import dev.bonygod.gymroutine.auth.domain.repository.AuthRepository

class DeleteAccountUseCase(private val repository: AuthRepository) {

    suspend operator fun invoke(): Result<Unit> {
        val hasRecentLogin = repository.hasRecentLogin().getOrElse { return Result.failure(it) }
        if (!hasRecentLogin) return Result.failure(AuthError.RecentLoginRequired())

        return repository.deleteAccount()
    }
}
