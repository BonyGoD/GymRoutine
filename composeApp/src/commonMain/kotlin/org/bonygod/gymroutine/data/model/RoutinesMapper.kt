package org.bonygod.gymroutine.data.model

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject

fun List<Routine>.routinesToFirestoreFormat(): JsonObject {

    val routinesJson = this.map { routine ->
        buildJsonObject {
            put("mapValue", buildJsonObject {
                put("fields", buildJsonObject {
                    put("id", buildJsonObject { put("stringValue", JsonPrimitive(routine.id)) })
                    put("type", buildJsonObject { put("stringValue", JsonPrimitive(routine.type)) })
                    put("repetitions", buildJsonObject { put("integerValue", JsonPrimitive(routine.repetitions)) })
                    put("sets", buildJsonObject { put("integerValue", JsonPrimitive(routine.sets)) })
                    put("rest", buildJsonObject { put("integerValue", JsonPrimitive(routine.rest)) })
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