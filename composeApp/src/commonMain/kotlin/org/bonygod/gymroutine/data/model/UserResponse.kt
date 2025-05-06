package org.bonygod.gymroutine.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
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
    val gender: String? = null,

    @SerialName("routines")
    val routines: List<Routine>? = null
) {
    companion object {
        fun fromFirestoreFormat(fields: JsonObject?): UserResponse {
            return UserResponse(
                userName = fields?.get("userName")?.jsonObject?.get("stringValue")?.jsonPrimitive?.contentOrNull ?: "",
                email = fields?.get("email")?.jsonObject?.get("stringValue")?.jsonPrimitive?.contentOrNull ?: "",
                age = fields?.get("age")?.jsonObject?.get("integerValue")?.jsonPrimitive?.contentOrNull?.toInt() ?: 0,
                height = fields?.get("height")?.jsonObject?.get("integerValue")?.jsonPrimitive?.contentOrNull?.toInt() ?: 0,
                weight = fields?.get("weight")?.jsonObject?.get("integerValue")?.jsonPrimitive?.contentOrNull?.toInt() ?: 0,
                gender = fields?.get("gender")?.jsonObject?.get("stringValue")?.jsonPrimitive?.contentOrNull ?: "",
                routines = fields?.get("routines")?.jsonObject?.get("mapValue")?.jsonObject?.get("fields")?.jsonObject?.map { (key, routineElement) ->
                    val routineFields = routineElement.jsonObject["mapValue"]?.jsonObject?.get("fields")?.jsonObject
                    Routine(
                        id = key,
                        type = routineFields?.get("type")?.jsonObject?.get("stringValue")?.jsonPrimitive?.contentOrNull ?: "",
                        repetitions = routineFields?.get("repetitions")?.jsonObject?.get("integerValue")?.jsonPrimitive?.contentOrNull?.toInt() ?: 0,
                        sets = routineFields?.get("sets")?.jsonObject?.get("integerValue")?.jsonPrimitive?.contentOrNull?.toInt() ?: 0,
                        rest = routineFields?.get("rest")?.jsonObject?.get("integerValue")?.jsonPrimitive?.contentOrNull?.toInt() ?: 0
                    )
                }?.toList()
            )
        }
    }
}