package dev.bonygod.gymroutine.auth.data.datasource

import dev.gitlive.firebase.auth.UserMetaData

private const val NS_REFERENCE_DATE_TO_UNIX_EPOCH_SECONDS = 978307200L

actual fun UserMetaData.lastSignInEpochMillis(): Long? =
    lastSignInTime?.let { seconds -> (seconds.toLong() + NS_REFERENCE_DATE_TO_UNIX_EPOCH_SECONDS) * 1000L }
