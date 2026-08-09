package dev.bonygod.gymroutine.workout.domain.model

data class WorkoutSession(
    val routineId: String,
    /** ISO-8601 "YYYY-MM-DD" del día al que pertenece la sesión. */
    val date: String,
    /** Nº de ejercicios de la rutina al guardar. Si no coincide al restaurar, la sesión se descarta. */
    val exerciseCount: Int,
    val exercises: List<ExerciseSessionProgress>,
)

data class ExerciseSessionProgress(
    val index: Int,
    val completedSets: Int,
    val isCompleted: Boolean,
    val isSkipped: Boolean,
)
