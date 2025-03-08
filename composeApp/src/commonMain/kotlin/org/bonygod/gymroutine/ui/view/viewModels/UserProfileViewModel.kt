package org.bonygod.gymroutine.ui.view.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.bonygod.gymroutine.data.model.User
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UserProfileViewModel: ViewModel(), KoinComponent {

    private val userViewModel: UserViewModel by inject()

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

    private val _user = MutableStateFlow(null as User?)
    val user = _user.asStateFlow()

    fun logOut(navigateToLoginOrSignup: () -> Unit) {
        viewModelScope.launch {
            val userDb = userViewModel.getUser().first()
            //Guardar datos en el viewModel
            _user.value = userDb
            userViewModel.deleteUser(userDb!!)
            navigateToLoginOrSignup()
        }
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