package org.bonygod.gymroutine.domain

import org.bonygod.gymroutine.data.repository.AuthRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ForgotPasswordUseCase: KoinComponent {

    private val authRepository: AuthRepository by inject()

    suspend operator fun invoke(email: String): String {
        return authRepository.resetPassword(email)
    }
}