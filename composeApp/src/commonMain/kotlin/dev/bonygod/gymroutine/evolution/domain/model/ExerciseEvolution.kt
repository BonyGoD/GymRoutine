package dev.bonygod.gymroutine.evolution.domain.model

import dev.bonygod.gymroutine.routines.domain.model.ExerciseProgress

/** Evolución agregada de un ejercicio a través de todas las rutinas donde aparece. */
data class ExerciseEvolution(
    val name: String,
    val routineNames: List<String>,
    /** Progresión ordenada de más antigua a más reciente, agregada de todas las rutinas. */
    val history: List<ExerciseProgress>,
) {
    private val first: ExerciseProgress? get() = history.firstOrNull()
    private val last: ExerciseProgress? get() = history.lastOrNull()

    val sessionCount: Int get() = history.size
    val firstWeight: Float get() = first?.weight ?: 0f
    val lastWeight: Float get() = last?.weight ?: 0f
    val firstReps: Int get() = first?.reps ?: 0
    val lastReps: Int get() = last?.reps ?: 0
    val weightDelta: Float get() = lastWeight - firstWeight
    val repsDelta: Int get() = lastReps - firstReps
}
