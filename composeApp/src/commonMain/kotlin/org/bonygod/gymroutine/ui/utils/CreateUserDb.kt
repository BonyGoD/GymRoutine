package org.bonygod.gymroutine.ui.utils

import org.bonygod.gymroutine.data.model.User

fun createUserDb(id: String, displayName: String, email: String, idToken: String): User {
    return User(
        id = id,
        displayName = displayName,
        email = email,
        token = idToken
    )
}