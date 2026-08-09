package dev.bonygod.gymroutine.routines.domain.model

data class Exercise(
    val name: String,
    val sets: Int,
    val restSeconds: Int,
    /** Progresión ordenada de más antigua a más reciente. Primero = inicial, último = actual. */
    val history: List<ExerciseProgress> = emptyList(),
) {
    val initial: ExerciseProgress? get() = history.firstOrNull()
    val current: ExerciseProgress? get() = history.lastOrNull()

    val weight: Float get() = current?.weight ?: 0f
    val reps: Int get() = current?.reps ?: 0
    val initialWeight: Float get() = initial?.weight ?: 0f
    val initialReps: Int get() = initial?.reps ?: 0
}

/**
 * Devuelve el ejercicio con un registro nuevo si [weight]/[reps] difieren del último;
 * el mismo ejercicio si coinciden. Idempotente: llamarlo N veces sobre el mismo
 * ejercicio base produce como máximo un registro nuevo.
 */
fun Exercise.withProgress(weight: Float, reps: Int, timestamp: Long): Exercise {
    val last = history.lastOrNull()
    if (last != null && last.weight == weight && last.reps == reps) return this
    return copy(history = history + ExerciseProgress(weight, reps, timestamp))
}
