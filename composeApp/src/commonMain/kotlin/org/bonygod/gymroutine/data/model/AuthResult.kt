package org.bonygod.gymroutine.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthResult(
    val idToken: String?,
    val email: String?,
    val displayName: String?,
    val localId: String?,
    val error: ErrorResponse? = null
)

@Serializable
data class ErrorResponse(val message: String?)

