package org.bonygod.gymroutine.data.model

import kotlinx.serialization.Serializable

@Serializable
data class FirebaseSignUpRequest (
    val email: String,
    val password: String,
    val displayName: String,
    val returnSecureToken: Boolean = true
)