package org.bonygod.gymroutine.domain

import org.bonygod.gymroutine.data.model.User
import org.bonygod.gymroutine.data.repository.UserRepository

class DeleteUserDaoUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User){
        userRepository.deleteUser(user)
    }
}