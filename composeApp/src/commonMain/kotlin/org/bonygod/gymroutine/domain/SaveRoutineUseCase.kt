package org.bonygod.gymroutine.domain

import org.bonygod.gymroutine.data.model.Routine
import org.bonygod.gymroutine.data.model.UserRequest
import org.bonygod.gymroutine.data.repository.UserDataRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SaveRoutineUseCase: KoinComponent {

    private val userDataRepository: UserDataRepository by inject()

    suspend operator fun invoke(userRequest: UserRequest, routines: List<Routine>) {
        return userDataRepository.addRoutines(userRequest, routines)
    }
}