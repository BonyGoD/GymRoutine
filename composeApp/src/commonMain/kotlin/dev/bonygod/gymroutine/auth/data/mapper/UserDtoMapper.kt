package dev.bonygod.gymroutine.auth.data.mapper

import dev.bonygod.gymroutine.auth.data.model.UserDto
import dev.gitlive.firebase.firestore.DocumentSnapshot

internal const val FIELD_UID = "uid"
internal const val FIELD_NAME = "name"
internal const val FIELD_AGE = "age"
internal const val FIELD_WEIGHT = "weight"
internal const val FIELD_HEIGHT = "height"
internal const val FIELD_EMAIL = "email"

internal fun DocumentSnapshot.toUserDto(fallbackUid: String): UserDto = UserDto(
    uid = get(FIELD_UID) as? String ?: fallbackUid,
    name = get(FIELD_NAME) as? String ?: "",
    age = get(FIELD_AGE) as? String ?: "",
    weight = get(FIELD_WEIGHT) as? String ?: "",
    height = get(FIELD_HEIGHT) as? String ?: "",
    email = get(FIELD_EMAIL) as? String ?: "",
)

internal fun UserDto.toMap(): Map<String, Any> = mapOf(
    FIELD_UID to uid,
    FIELD_NAME to name,
    FIELD_AGE to age,
    FIELD_WEIGHT to weight,
    FIELD_HEIGHT to height,
    FIELD_EMAIL to email,
)
