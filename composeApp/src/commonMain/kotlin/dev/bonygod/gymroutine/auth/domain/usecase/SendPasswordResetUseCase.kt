package dev.bonygod.gymroutine.auth.domain.usecase

import dev.bonygod.gymroutine.auth.domain.repository.AuthRepository

class SendPasswordResetUseCase(private val repository: AuthRepository) {

    suspend operator fun invoke(email: String): Result<Unit> = repository.sendPasswordReset(email = email)
}
