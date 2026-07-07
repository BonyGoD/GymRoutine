package dev.bonygod.gymroutine.routines.domain.error

sealed class RoutineError(message: String) : Exception(message) {
    class RoutineNotFound : RoutineError("Rutina no encontrada")
    class NetworkError : RoutineError("Error de red. Comprueba tu conexión")
    class Unauthorized : RoutineError("No autorizado")
    class UnknownError(cause: String = "Error desconocido") : RoutineError(cause)
}
