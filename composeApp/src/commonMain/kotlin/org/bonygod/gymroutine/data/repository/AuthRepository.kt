package org.bonygod.gymroutine.data.repository

import org.bonygod.gymroutine.data.model.AuthResult
import org.bonygod.gymroutine.data.network.AuthenticationService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AuthRepository(): KoinComponent {

    private val authenticationService: AuthenticationService by inject()

    suspend fun login(email: String, password: String): AuthResult {
        return authenticationService.login(email, password)
    }

    suspend fun signUp(email: String, password: String, displayName: String): AuthResult {
        return authenticationService.signUp(email, password, displayName)
    }
}
