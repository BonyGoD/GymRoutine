package org.bonygod.gymroutine.ui.view.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.bonygod.gymroutine.data.model.User
import org.bonygod.gymroutine.data.model.UserDataFirestore
import org.bonygod.gymroutine.domain.DeleteUserDaoUseCase
import org.bonygod.gymroutine.domain.GetUserDaoUseCase
import org.bonygod.gymroutine.domain.GetUserUseCase
import org.bonygod.gymroutine.domain.SaveUserDataUseCase
import org.bonygod.gymroutine.domain.UpdateUserDaoUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UserProfileViewModel : ViewModel(), KoinComponent {

    private val getUserDaoUseCase: GetUserDaoUseCase by inject()
    private val deleteUserDaoUseCase: DeleteUserDaoUseCase by inject()
    private val saveUserDataUseCase: SaveUserDataUseCase by inject()
    private val getUserDataUseCase: GetUserUseCase by inject()
    private val updateUserDaoUseCase: UpdateUserDaoUseCase by inject()

    private val _selectedWeight = MutableStateFlow(58)
    val selectedWeight = _selectedWeight

    private val _selectedHeight = MutableStateFlow(170)
    val selectedHeight = _selectedHeight

    private val _selectedAge = MutableStateFlow(25)
    val selectedAge = _selectedAge

    private val _selectedGender = MutableStateFlow("Hombre")
    val selectedGender = _selectedGender

    private val _inisitalValue = MutableStateFlow(true)
    val inisitalValue = _inisitalValue

    private val _userDao = MutableStateFlow(null as User?)
    val userDao = _userDao

    private val _userData = MutableStateFlow(null as UserDataFirestore?)
    val userData = _userData

    private val _shouldNavigate = MutableStateFlow(false)
    val shouldNavigate = _shouldNavigate

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading

    fun logOut() {
        viewModelScope.launch {
            val userDb = getUserDaoUseCase().first()
            deleteUserDaoUseCase(userDb!!)
        }
    }

    fun saveUserData() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                withContext(Dispatchers.Main) {
                    val userDb = getUserDaoUseCase().first()
                    // Guardar datos en el viewModel
                    _userDao.value = userDb
                    if (userDb != null) {
                        saveUserDataUseCase(
                            userDb.id,
                            userDb.displayName,
                            selectedWeight.value,
                            selectedHeight.value,
                            selectedAge.value,
                            selectedGender.value,
                            userDb.email
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _shouldNavigate.value = true
            }
        }
    }

    suspend fun getUserData(userId: String) {
        try {
            withContext(Dispatchers.Main) {
                val userDataFirestore = getUserDataUseCase(userId)
                _userData.value = userDataFirestore
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateUserDaoData() {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val userDb = getUserDaoUseCase().first()
                    if (userDb != null) {
                        val upDateUser = User(
                            userDb.id,
                            userDb.email,
                            _userData.value?.userName ?: "",
                            userDb.token
                        )
                        updateUserDaoUseCase(upDateUser)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun resetNavigationFlag() {
        _shouldNavigate.value = false
    }

    fun selectedWeight(selected: Int) {
        _selectedWeight.value = selected
    }

    fun selectedHeight(selected: Int) {
        _selectedHeight.value = selected
    }

    fun selectedAge(selected: Int) {
        _selectedAge.value = selected
    }

    fun selectedGender(selected: String) {
        _selectedGender.value = selected
    }

    fun inisitalValue(selected: Boolean) {
        _inisitalValue.value = selected
    }
}