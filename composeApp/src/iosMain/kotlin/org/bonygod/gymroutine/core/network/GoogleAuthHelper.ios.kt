package org.bonygod.gymroutine.core.network

actual class GoogleAuthHelper {
    actual suspend fun signInWithGoogle(
        onSuccess: (String, String, String, String, String) -> Unit,
        onError: (String) -> Unit,
    ) {
    }
}