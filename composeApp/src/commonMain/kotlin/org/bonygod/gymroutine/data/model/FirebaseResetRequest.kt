package org.bonygod.gymroutine.data.model

import kotlinx.serialization.Serializable

@Serializable
data class FirebaseResetRequest(
    val email: String
)