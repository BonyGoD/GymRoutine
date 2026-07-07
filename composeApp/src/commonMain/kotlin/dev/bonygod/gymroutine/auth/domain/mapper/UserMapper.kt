package dev.bonygod.gymroutine.auth.domain.mapper

import dev.bonygod.gymroutine.auth.data.model.UserDto
import dev.bonygod.gymroutine.auth.domain.model.User

fun UserDto.toDomain(): User = User(
    uid = uid,
    name = name,
    age = age,
    weight = weight,
    height = height,
    email = email,
)

fun User.toDto(): UserDto = UserDto(
    uid = uid,
    name = name,
    age = age,
    weight = weight,
    height = height,
    email = email,
)
