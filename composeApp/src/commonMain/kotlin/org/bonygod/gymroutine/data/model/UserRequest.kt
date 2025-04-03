package org.bonygod.gymroutine.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject

@Serializable
data class UserRequest(
    val id: String,
    val userName: String,
    val weight: Int,
    val height: Int,
    val age: Int,
    val gender: String,
    val email: String
) {
    fun toFirestoreFormat(): JsonObject {
        return buildJsonObject {
            put("fields", buildJsonObject {
                put("userName", buildJsonObject { put("stringValue", JsonPrimitive(userName)) })
                put("height", buildJsonObject { put("integerValue", JsonPrimitive(height)) })
                put("weight", buildJsonObject { put("integerValue", JsonPrimitive(weight)) })
                put("age", buildJsonObject { put("integerValue", JsonPrimitive(age)) })
                put("gender", buildJsonObject { put("stringValue", JsonPrimitive(gender)) })
                put("email", buildJsonObject { put("stringValue", JsonPrimitive(email)) })
            })
        }
    }
}