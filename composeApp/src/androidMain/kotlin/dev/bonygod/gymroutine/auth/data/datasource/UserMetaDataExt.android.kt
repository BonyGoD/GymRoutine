package dev.bonygod.gymroutine.auth.data.datasource

import dev.gitlive.firebase.auth.UserMetaData

actual fun UserMetaData.lastSignInEpochMillis(): Long? = lastSignInTime?.toLong()
