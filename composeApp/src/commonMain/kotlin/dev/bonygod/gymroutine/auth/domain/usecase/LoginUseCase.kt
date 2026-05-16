package dev.bonygod.gymroutine.auth.domain.usecase

import dev.bonygod.gymroutine.auth.domain.model.User
import dev.bonygod.gymroutine.auth.domain.repository.AuthRepository

class LoginUseCase(private val repository: AuthRepository) {

    suspend operator fun invoke(email: String, password: String): Result<User> = repository.login(email = email, password = password)
}
