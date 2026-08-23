package dev.bonygod.gymroutine.auth.domain.usecase

import dev.bonygod.gymroutine.auth.domain.model.User
import dev.bonygod.gymroutine.auth.domain.repository.AuthRepository

class UpdateUserProfileUseCase(private val repository: AuthRepository) {

    suspend operator fun invoke(
        uid: String,
        age: String,
        weight: String,
        height: String,
    ): Result<User> = repository.updateUserProfile(
        uid = uid,
        age = age,
        weight = weight,
        height = height,
    )
}
