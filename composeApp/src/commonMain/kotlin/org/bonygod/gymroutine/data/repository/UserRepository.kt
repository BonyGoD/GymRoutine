package org.bonygod.gymroutine.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import org.bonygod.gymroutine.data.localDb.RoomDb
import org.bonygod.gymroutine.data.model.User

class UserRepository(
    private val database: RoomDb
) {

    private val dispatcher = Dispatchers.IO

    suspend fun insertUser(user: User) {
        with(dispatcher) {
            database.userDao().insertUser(user)
        }
    }

    suspend fun updateUser(user: User){
        with(dispatcher){
            database.userDao().updateUser(user)
        }
    }

    suspend fun deleteUser(user: User){
        with(dispatcher){
            database.userDao().deleteUser(user)
        }
    }

    fun getUser(): Flow<User?> {
        return database.userDao().getUser()
    }
}