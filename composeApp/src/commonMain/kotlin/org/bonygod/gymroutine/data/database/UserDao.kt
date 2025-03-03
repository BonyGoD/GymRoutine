package org.bonygod.gymroutine.data.database

import kotlinx.coroutines.flow.Flow

interface UserDao {

    fun fetchUserById(id: Int): Flow<User>

    suspend fun insertUser(user: User)
}