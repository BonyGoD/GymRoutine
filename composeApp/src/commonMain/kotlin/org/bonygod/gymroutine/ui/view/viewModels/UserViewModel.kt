package org.bonygod.gymroutine.ui.view.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.bonygod.gymroutine.data.model.User
import org.bonygod.gymroutine.domain.DeleteUserUseCase
import org.bonygod.gymroutine.domain.GetUserUseCase
import org.bonygod.gymroutine.domain.InsertUserUseCase
import org.bonygod.gymroutine.domain.UpdateUserUseCase

class UserViewModel(
    private val insertUserUseCase: InsertUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val getUserUseCase: GetUserUseCase
):ViewModel() {

    val insertUser = fun(user: User) {
        viewModelScope.launch { insertUserUseCase(user) }
    }

    val updateUser = fun(user: User) {
        viewModelScope.launch { updateUserUseCase(user) }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch { deleteUserUseCase(user) }
    }

    fun getUser(): Flow<User?> {
        return getUserUseCase()
    }
}