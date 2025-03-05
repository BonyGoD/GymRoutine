package org.bonygod.gymroutine.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthResult(

    @SerialName("idToken")
    val token: String? = null,

    @SerialName("email")
    val email: String? = null,

    @SerialName("displayName")
    val displayName: String? = null,

    @SerialName("localId")
    var uid: String? = null,

    @SerialName("error")
    val error: FirebaseErrorResponse? = null
)

