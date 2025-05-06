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
    val email: String,
    val routines: List<Routine>
) {
    fun toFirestoreFormat(): JsonObject {
        val defaultRoutine = listOf(
            Routine("default1", "Fuerza", 12, 4, 60),
            Routine("default2", "Cardio", 15, 3, 30)
        )
        return buildJsonObject {
            put("fields", buildJsonObject {
                put("userName", buildJsonObject { put("stringValue", JsonPrimitive(userName)) })
                put("height", buildJsonObject { put("integerValue", JsonPrimitive(height)) })
                put("weight", buildJsonObject { put("integerValue", JsonPrimitive(weight)) })
                put("age", buildJsonObject { put("integerValue", JsonPrimitive(age)) })
                put("gender", buildJsonObject { put("stringValue", JsonPrimitive(gender)) })
                put("email", buildJsonObject { put("stringValue", JsonPrimitive(email)) })
                put("routines", buildJsonObject {
                    put("mapValue", buildJsonObject {
                        put("fields", buildJsonObject {
                            defaultRoutine.forEach { routine ->
                                put(routine.id, buildJsonObject {
                                    put("mapValue", buildJsonObject {
                                        put("fields", buildJsonObject {
                                            put("type", buildJsonObject { put("stringValue", JsonPrimitive("Fuerza")) })
                                            put("repetitions", buildJsonObject { put("integerValue", JsonPrimitive(12)) })
                                            put("sets", buildJsonObject { put("integerValue", JsonPrimitive(4)) })
                                            put("rest", buildJsonObject { put("integerValue", JsonPrimitive(60)) })
                                        })
                                    })
                                })
                            }
                        })
                    })
                })
            })
        }
    }
}