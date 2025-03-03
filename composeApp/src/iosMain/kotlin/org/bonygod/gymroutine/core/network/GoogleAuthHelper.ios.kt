package org.bonygod.gymroutine.core.network

actual class GoogleAuthHelper {
    actual suspend fun signInWithGoogle(
        onSuccess: (String, String) -> Unit,
        onError: (String) -> Unit,
    ) {
    }
}