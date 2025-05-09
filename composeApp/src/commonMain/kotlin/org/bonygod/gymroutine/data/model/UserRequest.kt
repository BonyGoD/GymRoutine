package org.bonygod.gymroutine.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonArray
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
        var exercices = listOf(
            Exercice("Triceps con polea de levantamiento de peso en ruso rumano",1,4,60),
            Exercice("xczvzxcv",1,4,60),
            Exercice("assdfasdf",1,4,60),
            Exercice("asdretf",1,4,60)
        )
        val defaultRoutine = listOf(
            Routine("Fuerza", "Fuerza", "Monday", exercices),
            Routine("Cardio", "Cardio", "Friday", exercices)
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
                                            put("type", buildJsonObject { put("stringValue", JsonPrimitive(routine.type)) })
                                            put("day", buildJsonObject { put("stringValue", JsonPrimitive(routine.type)) })
                                            put("exercises", buildJsonObject {
                                                put("arrayValue", buildJsonObject {
                                                    put("values", buildJsonArray {
                                                        routine.exercises.forEach { exercise ->
                                                            add(buildJsonObject {
                                                                put("mapValue", buildJsonObject {
                                                                    put("fields", buildJsonObject {
                                                                        put("name", buildJsonObject { put("stringValue", JsonPrimitive(exercise.name)) })
                                                                        put("repetitions", buildJsonObject { put("integerValue", JsonPrimitive(exercise.repetitions)) })
                                                                        put("sets", buildJsonObject { put("integerValue", JsonPrimitive(exercise.sets)) })
                                                                        put("rest", buildJsonObject { put("integerValue", JsonPrimitive(exercise.rest)) })
                                                                    })
                                                                })
                                                            })
                                                        }
                                                    })
                                                })
                                            })
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