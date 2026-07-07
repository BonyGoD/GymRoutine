package dev.bonygod.gymroutine.auth.domain.usecase

import dev.bonygod.gymroutine.auth.domain.model.User
import dev.bonygod.gymroutine.auth.domain.repository.AuthRepository

class RegisterUseCase(private val repository: AuthRepository) {

    suspend operator fun invoke(
        email: String,
        password: String,
        name: String,
        age: String,
        weight: String,
        height: String,
    ): Result<User> = repository.register(
        email = email,
        password = password,
        name = name,
        age = age,
        weight = weight,
        height = height,
    )
}
