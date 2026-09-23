package dev.bonygod.gymroutine.auth.domain.error

sealed class AuthError(message: String) : Exception(message) {
    class InvalidCredentials : AuthError("Correo o contraseña incorrectos")
    class UserNotFound : AuthError("Usuario no encontrado")
    class EmailAlreadyInUse : AuthError("El correo electrónico ya está en uso")
    class NetworkError : AuthError("Error de red. Comprueba tu conexión")
    class Unauthorized : AuthError("No autorizado")
    class RecentLoginRequired : AuthError("Debes volver a iniciar sesión para continuar")
    class UnknownError(cause: String = "Error desconocido") : AuthError(cause)
}
