package org.bonygod.gymroutine.domain

import org.bonygod.gymroutine.data.model.UserData
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
        val userData = UserData(id, displayName, weight, height, age, gender, email)
        return userDataRepository.saveUserData(userData)
    }
}