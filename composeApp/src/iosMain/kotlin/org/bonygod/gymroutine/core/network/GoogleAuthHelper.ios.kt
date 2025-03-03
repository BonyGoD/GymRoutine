package org.bonygod.gymroutine.core.network

actual class GoogleAuthHelper {
    actual suspend fun signInWithGoogle(
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit,
    ) {
    }
}