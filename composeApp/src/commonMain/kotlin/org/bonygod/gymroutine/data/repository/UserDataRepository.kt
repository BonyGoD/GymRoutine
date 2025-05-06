package org.bonygod.gymroutine.data.repository

import org.bonygod.gymroutine.data.mapper.toDomainModel
import org.bonygod.gymroutine.data.model.Routine
import org.bonygod.gymroutine.data.model.UserDataFirestore
import org.bonygod.gymroutine.data.model.UserRequest
import org.bonygod.gymroutine.data.model.UserResponse
import org.bonygod.gymroutine.data.network.UserDataService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UserDataRepository : KoinComponent {

    private val userDataService: UserDataService by inject()

    suspend fun saveUserData(userRequest: UserRequest) {
        return userDataService.saveUser(userRequest)
    }

    suspend fun getUserData(userId: String): UserDataFirestore {
        return userDataService.getUser(userId).toDomainModel()
    }

    suspend fun addRoutines(userRequest: UserRequest, routines: List<Routine>) {
        return userDataService.addRoutines(userRequest, routines)
    }
}