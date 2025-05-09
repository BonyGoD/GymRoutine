package org.bonygod.gymroutine.data.mapper

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import org.bonygod.gymroutine.data.model.Routine

fun List<Routine>.routinesToFirestoreFormat(): JsonObject {

    val routinesJson = this.map { routine ->
        buildJsonObject {
            put("mapValue", buildJsonObject {
                put("fields", buildJsonObject {
                    put("id", buildJsonObject { put("stringValue", JsonPrimitive(routine.id)) })
                    put("type", buildJsonObject { put("stringValue", JsonPrimitive(routine.type)) })
                    put("day", buildJsonObject { put("stringValue", JsonPrimitive(routine.day)) })
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
        }
    }

    return buildJsonObject {
        put("fields", buildJsonObject {
            put("routines", buildJsonObject {
                put("arrayValue", buildJsonObject {
                    put("values", JsonArray(routinesJson))
                })
            })
        })
    }
}