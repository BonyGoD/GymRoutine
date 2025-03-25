package org.bonygod.gymroutine.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.bonygod.gymroutine.data.model.UserData
import org.bonygod.gymroutine.data.network.UserDataService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UserDataRepository : KoinComponent {

    private val userDataService: UserDataService by inject()

    suspend fun saveUserData(userData: UserData) {
        return userDataService.saveUser(userData)
    }
}