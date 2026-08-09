package dev.bonygod.gymroutine.routines.ui.mapper

import dev.bonygod.gymroutine.routines.domain.model.Exercise
import dev.bonygod.gymroutine.routines.domain.model.ExerciseProgress
import dev.bonygod.gymroutine.routines.domain.model.withProgress
import dev.bonygod.gymroutine.routines.ui.model.ExerciseForm
import kotlin.time.Clock

// Ejercicios antiguos guardados con restSeconds = 0 deben poder editarse: rellenamos con un
// valor por defecto en vez de dejar el campo vacío, que bloquearía el formulario (isValid exige > 0).
private const val DEFAULT_REST_SECONDS = 60

fun ExerciseForm.toExercise(timestamp: Long = Clock.System.now().toEpochMilliseconds()): Exercise {
    val formWeight = weight.toFloatOrNull() ?: 0f
    val formReps = reps.toIntOrNull() ?: 0
    val formSets = sets.toIntOrNull() ?: 0
    val formRestSeconds = restSeconds.toIntOrNull() ?: 0
    return if (origin == null) {
        Exercise(
            name = name,
            sets = formSets,
            restSeconds = formRestSeconds,
            history = listOf(ExerciseProgress(weight = formWeight, reps = formReps, timestamp = timestamp)),
        )
    } else {
        origin.withProgress(weight = formWeight, reps = formReps, timestamp = timestamp).copy(
            name = name,
            sets = formSets,
            restSeconds = formRestSeconds,
        )
    }
}

fun Exercise.toForm() = ExerciseForm(
    name = name,
    sets = if (sets == 0) "" else sets.toString(),
    reps = if (reps == 0) "" else reps.toString(),
    weight = if (weight == 0f) "" else weight.toString(),
    restSeconds = if (restSeconds == 0) DEFAULT_REST_SECONDS.toString() else restSeconds.toString(),
    origin = this,
)
