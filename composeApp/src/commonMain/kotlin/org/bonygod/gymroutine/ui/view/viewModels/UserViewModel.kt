package org.bonygod.gymroutine.ui.view.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.bonygod.gymroutine.data.model.User
import org.bonygod.gymroutine.domain.DeleteUserDaoUseCase
import org.bonygod.gymroutine.domain.GetUserDaoUseCase
import org.bonygod.gymroutine.domain.InsertUserDaoUseCase
import org.bonygod.gymroutine.domain.UpdateUserDaoUseCase

class UserViewModel(
    private val insertUserDaoUseCase: InsertUserDaoUseCase,
    private val updateUserDaoUseCase: UpdateUserDaoUseCase,
    private val deleteUserDaoUseCase: DeleteUserDaoUseCase,
    private val getUserDaoUseCase: GetUserDaoUseCase
):ViewModel() {

    val insertUser = fun(user: User) {
        viewModelScope.launch { insertUserDaoUseCase(user) }
    }

    val updateUser = fun(user: User) {
        viewModelScope.launch {
            updateUserDaoUseCase(user)
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch { deleteUserDaoUseCase(user) }
    }

    fun getUser(): Flow<User?> {
        return getUserDaoUseCase()
    }
}