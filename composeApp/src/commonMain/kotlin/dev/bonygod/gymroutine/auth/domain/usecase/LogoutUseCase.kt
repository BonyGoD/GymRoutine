package dev.bonygod.gymroutine.auth.domain.usecase

import dev.bonygod.gymroutine.auth.domain.repository.AuthRepository

class LogoutUseCase(private val repository: AuthRepository) {

    suspend operator fun invoke(): Result<Unit> = repository.logout()
}
