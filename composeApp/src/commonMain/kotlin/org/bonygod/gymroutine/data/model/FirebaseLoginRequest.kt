package org.bonygod.gymroutine.data.model

import kotlinx.serialization.Serializable

@Serializable
data class FirebaseLoginRequest(
    val email: String,
    val password: String,
    val returnSecureToken: Boolean = true
)