package dev.bonygod.gymroutine.auth.data.mapper

import dev.bonygod.gymroutine.auth.domain.error.AuthError

internal fun Exception.toAuthError(): AuthError = when {
    containsAny(
        "INVALID_LOGIN_CREDENTIALS",
        "INVALID_PASSWORD",
        "wrong-password",
        "invalid-credential",
        "auth credential is incorrect",
        "malformed or has expired",
    ) -> AuthError.InvalidCredentials()
    containsAny("user-not-found", "USER_NOT_FOUND") ->
        AuthError.UserNotFound()
    containsAny("email address is already in use", "email-already-in-use", "EMAIL_EXISTS") ->
        AuthError.EmailAlreadyInUse()
    containsAny("network", "NETWORK_REQUEST_FAILED", "timeout") ->
        AuthError.NetworkError()
    else ->
        AuthError.UnknownError(message ?: "Error desconocido")
}

private fun Exception.containsAny(vararg keywords: String): Boolean = keywords.any { message?.contains(it, ignoreCase = true) == true }
