package org.bonygod.gymroutine.domain

import org.bonygod.gymroutine.data.model.UserDataFirestore
import org.bonygod.gymroutine.data.repository.UserDataRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetUserUseCase: KoinComponent {

    private val userDataRepository: UserDataRepository by inject()

    suspend operator fun invoke(userId: String): UserDataFirestore {
        return userDataRepository.getUserData(userId)
    }
}