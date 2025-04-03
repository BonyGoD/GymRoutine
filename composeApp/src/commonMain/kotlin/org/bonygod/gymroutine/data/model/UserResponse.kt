package org.bonygod.gymroutine.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.int
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Serializable
data class UserResponse(
    @SerialName("userName")
    val userName: String? = null,

    @SerialName("email")
    val email: String? = null,

    @SerialName("weight")
    val weight: Int? = null,

    @SerialName("height")
    val height: Int? = null,

    @SerialName("age")
    val age: Int? = null,

    @SerialName("gender")
    val gender: String? = null
) {
    companion object {
        fun fromFirestoreFormat(fields: JsonObject?): UserResponse {
            return UserResponse(
                userName = fields?.get("userName")?.jsonObject?.get("stringValue")?.jsonPrimitive?.contentOrNull ?: "",
                email = fields?.get("email")?.jsonObject?.get("stringValue")?.jsonPrimitive?.contentOrNull ?: "",
                age = fields?.get("age")?.jsonObject?.get("integerValue")?.jsonPrimitive?.contentOrNull?.toInt() ?: 0,
                height = fields?.get("height")?.jsonObject?.get("integerValue")?.jsonPrimitive?.contentOrNull?.toInt() ?: 0,
                weight = fields?.get("weight")?.jsonObject?.get("integerValue")?.jsonPrimitive?.contentOrNull?.toInt() ?: 0,
                gender = fields?.get("gender")?.jsonObject?.get("stringValue")?.jsonPrimitive?.contentOrNull ?: ""
            )
        }
    }
}