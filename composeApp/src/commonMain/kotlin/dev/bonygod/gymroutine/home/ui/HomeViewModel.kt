package dev.bonygod.gymroutine.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bonygod.gymroutine.auth.domain.usecase.GetCurrentUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getCurrentUser: GetCurrentUserUseCase,
) : ViewModel() {

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName.asStateFlow()

    init {
        loadUser()
    }

    private fun loadUser() {
        viewModelScope.launch {
            getCurrentUser()
                .onSuccess { user -> _userName.value = user?.name.orEmpty() }
        }
    }
}
