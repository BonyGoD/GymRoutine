package org.bonygod.gymroutine.data.model

import kotlinx.serialization.Serializable

@Serializable
data class FirebaseErrorResponse(
    val error: FirebaseError
)

@Serializable
data class FirebaseError(
    val code: Int,
    val message: String
)