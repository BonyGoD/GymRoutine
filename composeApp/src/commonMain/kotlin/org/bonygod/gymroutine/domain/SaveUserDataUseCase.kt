package org.bonygod.gymroutine.domain

import org.bonygod.gymroutine.data.model.UserRequest
import org.bonygod.gymroutine.data.repository.UserDataRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SaveUserDataUseCase: KoinComponent {

    private val userDataRepository: UserDataRepository by inject()

    suspend operator fun invoke(
        id: String,
        displayName: String,
        weight: Int,
        height: Int,
        age: Int,
        gender: String,
        email: String
    ) {
        val userRequest = UserRequest(id, displayName, weight, height, age, gender, email, emptyList())
        return userDataRepository.saveUserData(userRequest)
    }
}