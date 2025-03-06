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

class PrimeraPantallaViewModel: ViewModel(), KoinComponent {

    private val userViewModel: UserViewModel by inject()

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

}