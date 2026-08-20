package dev.bonygod.gymroutine.workout.domain.model

data class WorkoutLog(
    val id: String,
    val routineId: String,
    val routineName: String,
    /** ISO-8601 date: "YYYY-MM-DD" */
    val date: String,
    val completado: Boolean = false,
    /**
     * Fecha ISO-8601 "YYYY-MM-DD" del día originalmente planificado, cuando este log es la
     * recuperación de un entreno saltado. `null` si no es una recuperación.
     */
    val recoveredFrom: String? = null,
)
