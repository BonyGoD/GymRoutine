package dev.bonygod.gymroutine.routines.ui.model

import dev.bonygod.gymroutine.routines.domain.model.Exercise

data class ExerciseForm(
    val name: String = "",
    val sets: String = "",
    val reps: String = "",
    val weight: String = "",
    val restSeconds: String = "",
    // Ejercicio del que salió el formulario; null si es nuevo. Solo transporta el historial.
    val origin: Exercise? = null,
) {
    // El peso queda opcional a propósito: los ejercicios de peso corporal son 0 kg legítimos.
    val isValid: Boolean
        get() = name.isNotBlank() && sets.isPositiveInt() && reps.isPositiveInt() && restSeconds.isPositiveInt()
}

private fun String.isPositiveInt() = (toIntOrNull() ?: 0) > 0
