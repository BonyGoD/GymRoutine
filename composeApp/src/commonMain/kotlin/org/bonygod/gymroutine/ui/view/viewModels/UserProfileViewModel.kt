package org.bonygod.gymroutine.ui.view.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.bonygod.gymroutine.data.model.User
import org.bonygod.gymroutine.domain.DeleteUserUseCase
import org.bonygod.gymroutine.domain.GetUserUseCase
import org.bonygod.gymroutine.domain.SaveUserDataUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UserProfileViewModel : ViewModel(), KoinComponent {

    private val getUserDaoUseCase: GetUserUseCase by inject()
    private val deleteUserDaoUseCase: DeleteUserUseCase by inject()
    private val userDataUseCase: SaveUserDataUseCase by inject()

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

    private val _userDaoModel = MutableStateFlow(null as User?)
    val user = _userDaoModel.asStateFlow()

    private val _shouldNavigate = MutableStateFlow(false)
    val shouldNavigate = _shouldNavigate

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading

    fun logOut(navigateToLoginOrSignup: () -> Unit) {
        viewModelScope.launch {
            val userDb = getUserDaoUseCase().first()
            //Guardar datos en el viewModel
            _userDaoModel.value = userDb
            deleteUserDaoUseCase(userDb!!)
            navigateToLoginOrSignup()
        }
    }

    fun saveUserData() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                withContext(Dispatchers.Main) {
                    val userDb = getUserDaoUseCase().first()
                    // Guardar datos en el viewModel
                    _userDaoModel.value = userDb
                    if (userDb != null) {
                        userDataUseCase(
                            user.value!!.id,
                            user.value!!.displayName,
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