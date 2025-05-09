package org.bonygod.gymroutine.ui.view.viewModels

import androidx.lifecycle.ViewModel
import org.bonygod.gymroutine.data.model.Routine
import org.bonygod.gymroutine.data.model.UserRequest
import org.bonygod.gymroutine.domain.SaveRoutineUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RoutinesViewModel: ViewModel(), KoinComponent {

    private val saveRoutineUseCase: SaveRoutineUseCase by inject()

    suspend fun saveRoutine(userRequest: UserRequest, routine: List<Routine>) {
        saveRoutineUseCase(userRequest, routine)
    }
}