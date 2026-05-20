package dev.bonygod.gymroutine.routines.ui.mapper

import dev.bonygod.gymroutine.routines.domain.model.Exercise
import dev.bonygod.gymroutine.routines.ui.model.ExerciseForm

fun ExerciseForm.toExercise() = Exercise(
    name = name,
    sets = sets.toIntOrNull() ?: 0,
    reps = reps.toIntOrNull() ?: 0,
    weight = weight.toFloatOrNull() ?: 0f,
    restSeconds = restSeconds.toIntOrNull() ?: 0,
)

fun Exercise.toForm() = ExerciseForm(
    name = name,
    sets = if (sets == 0) "" else sets.toString(),
    reps = if (reps == 0) "" else reps.toString(),
    weight = if (weight == 0f) "" else weight.toString(),
    restSeconds = if (restSeconds == 0) "" else restSeconds.toString(),
)
