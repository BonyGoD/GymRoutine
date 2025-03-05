package org.bonygod.gymroutine.domain

import kotlinx.coroutines.flow.Flow
import org.bonygod.gymroutine.data.model.User
import org.bonygod.gymroutine.data.repository.UserRepository

class GetUserUseCase(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<User?> {
        return userRepository.getUser()
    }
}