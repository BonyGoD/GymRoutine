package org.bonygod.gymroutine.domain

import org.bonygod.gymroutine.data.model.User
import org.bonygod.gymroutine.data.repository.UserRepository

class DeleteUserUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User){
        userRepository.deleteUser(user)
    }
}