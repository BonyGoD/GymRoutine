package org.bonygod.gymroutine.domain

import org.bonygod.gymroutine.data.model.AuthResult
import org.bonygod.gymroutine.data.repositories.AuthRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SignUpUseCase: KoinComponent {

    private val authRepository: AuthRepository by inject()

    suspend operator fun invoke(email: String, password: String, displayName: String): AuthResult {
        return authRepository.signUp(email, password, displayName)
    }
}