package org.bonygod.gymroutine.data.mapper

import org.bonygod.gymroutine.data.model.UserDataFirestore
import org.bonygod.gymroutine.data.model.UserResponse

fun UserResponse.toDomainModel(): UserDataFirestore {
    return UserDataFirestore(
        userName = this.userName ?: "",
        email = this.email ?: "",
        weight = this.weight ?: 0,
        height = this.height ?: 0,
        age = this.age ?: 0,
        gender = this.gender ?: ""
    )
}