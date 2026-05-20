package dev.bonygod.gymroutine.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bonygod.gymroutine.auth.domain.usecase.GetCurrentUserUseCase
import dev.bonygod.gymroutine.routines.domain.model.Routine
import dev.bonygod.gymroutine.routines.domain.usecase.ObserveRoutinesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getCurrentUser: GetCurrentUserUseCase,
    private val observeRoutines: ObserveRoutinesUseCase,
) : ViewModel() {

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _routines = MutableStateFlow<List<Routine>>(emptyList())
    val routines: StateFlow<List<Routine>> = _routines.asStateFlow()

    init {
        viewModelScope.launch {
            getCurrentUser()
                .onSuccess { user ->
                    _userName.value = user?.name.orEmpty()
                    val userId = user?.uid.orEmpty()
                    if (userId.isNotEmpty()) {
                        observeRoutines(userId)
                            .catch { /* mantener último valor conocido */ }
                            .collect { _routines.value = it }
                    }
                }
        }
    }
}
