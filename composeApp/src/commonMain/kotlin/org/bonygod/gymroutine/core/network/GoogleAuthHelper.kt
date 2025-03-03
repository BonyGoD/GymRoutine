package org.bonygod.gymroutine.core.network

expect class GoogleAuthHelper {
    suspend fun signInWithGoogle(onSuccess: (String) -> Unit, onError: (String) -> Unit)
}
