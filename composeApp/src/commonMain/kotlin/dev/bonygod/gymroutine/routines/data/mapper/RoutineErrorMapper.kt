package dev.bonygod.gymroutine.routines.data.mapper

import dev.bonygod.gymroutine.routines.domain.error.RoutineError

internal fun Exception.toRoutineError(): RoutineError {
    val msg = message?.lowercase() ?: ""
    return when {
        msg.containsAny("not-found", "not found", "no document")
        -> RoutineError.RoutineNotFound()
        msg.containsAny("network", "timeout", "unavailable")
        -> RoutineError.NetworkError()
        msg.containsAny("permission", "unauthorized", "unauthenticated")
        -> RoutineError.Unauthorized()
        else -> RoutineError.UnknownError(message ?: "Error desconocido")
    }
}

internal fun <T> Result<T>.mapError(): Result<T> = fold(
    onSuccess = { Result.success(it) },
    onFailure = { throwable ->
        if (throwable is Exception) {
            Result.failure(throwable.toRoutineError())
        } else {
            Result.failure(throwable)
        }
    },
)

private fun String.containsAny(vararg keywords: String) = keywords.any { this.contains(it, ignoreCase = true) }
