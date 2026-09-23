package dev.bonygod.gymroutine.auth.data.datasource

import dev.gitlive.firebase.auth.UserMetaData

expect fun UserMetaData.lastSignInEpochMillis(): Long?
