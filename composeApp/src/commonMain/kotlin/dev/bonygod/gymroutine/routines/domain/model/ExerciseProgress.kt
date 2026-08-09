package dev.bonygod.gymroutine.routines.domain.model

data class ExerciseProgress(
    val weight: Float,
    val reps: Int,
    /** Epoch millis. 0L = registro migrado de un documento antiguo sin fecha. */
    val timestamp: Long,
)
