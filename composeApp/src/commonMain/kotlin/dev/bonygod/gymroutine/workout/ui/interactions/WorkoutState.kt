package dev.bonygod.gymroutine.workout.ui.interactions

import dev.bonygod.gymroutine.routines.domain.model.Exercise
import dev.bonygod.gymroutine.workout.ui.model.ExerciseWorkoutForm

data class WorkoutState(
    val isLogging: Boolean = false,
    val exercises: List<Exercise> = emptyList(),
    val expandedExerciseIndex: Int? = null,
    val completedExercises: Set<Int> = emptySet(),
    val exerciseForms: Map<Int, ExerciseWorkoutForm> = emptyMap(),
) {
    val allExercisesCompleted: Boolean
        get() = exercises.isNotEmpty() && completedExercises.size == exercises.size

    fun showLogging() = copy(isLogging = true)

    fun setExercises(exercises: List<Exercise>) = copy(
        exercises = exercises,
        exerciseForms = exercises.mapIndexed { i, ex ->
            i to ExerciseWorkoutForm(
                weight = if (ex.weight == 0f) "" else ex.weight.toString(),
                reps = if (ex.reps == 0) "" else ex.reps.toString(),
            )
        }.toMap(),
    )

    fun toggleExpanded(index: Int) = copy(
        expandedExerciseIndex = if (expandedExerciseIndex == index) null else index,
    )

    fun completeExercise(index: Int) = copy(
        completedExercises = completedExercises + index,
        expandedExerciseIndex = null,
    )

    fun updateWeight(index: Int, weight: String) = copy(
        exerciseForms = exerciseForms + (
            index to (exerciseForms[index]?.copy(weight = weight) ?: ExerciseWorkoutForm(weight = weight))
            ),
    )

    fun updateReps(index: Int, reps: String) = copy(
        exerciseForms = exerciseForms + (
            index to (exerciseForms[index]?.copy(reps = reps) ?: ExerciseWorkoutForm(reps = reps))
            ),
    )
}
