package dev.bonygod.gymroutine.auth.domain.usecase

import dev.bonygod.gymroutine.auth.domain.model.User
import dev.bonygod.gymroutine.auth.domain.repository.AuthRepository

class UpdateUserNameUseCase(private val repository: AuthRepository) {

    suspend operator fun invoke(uid: String, name: String): Result<User> =
        repository.updateUserName(uid = uid, name = name)
}
