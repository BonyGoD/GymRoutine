package dev.bonygod.gymroutine.watch.domain.usecase

import dev.bonygod.gymroutine.routines.domain.model.withProgress
import dev.bonygod.gymroutine.routines.domain.usecase.GetRoutinesUseCase
import dev.bonygod.gymroutine.routines.domain.usecase.UpdateRoutineUseCase
import dev.bonygod.gymroutine.watch.domain.model.WatchWorkoutResult
import dev.bonygod.gymroutine.workout.domain.usecase.ClearWorkoutSessionUseCase
import dev.bonygod.gymroutine.workout.domain.usecase.LogWorkoutUseCase

class ApplyWatchWorkoutUseCase(
    private val getRoutines: GetRoutinesUseCase,
    private val updateRoutine: UpdateRoutineUseCase,
    private val logWorkout: LogWorkoutUseCase,
    private val clearWorkoutSession: ClearWorkoutSessionUseCase,
) {

    suspend operator fun invoke(userId: String, result: WatchWorkoutResult): Result<Unit> = runCatching {
        val routines = getRoutines(userId).getOrThrow()
        val routine = routines.find { it.id == result.routineId }
        if (routine != null) {
            val updatedExercises = routine.exercises.mapIndexed { index, exercise ->
                val exerciseResult = result.exercises.find { it.index == index && it.name == exercise.name }
                if (exerciseResult != null && !exerciseResult.skipped && exerciseResult.completedSets > 0) {
                    exercise.withProgress(exerciseResult.weight, exerciseResult.reps, result.finishedAt)
                } else {
                    exercise
                }
            }
            if (updatedExercises != routine.exercises) {
                updateRoutine(userId, routine.copy(exercises = updatedExercises)).getOrThrow()
            }
        }
        logWorkout(
            userId = userId,
            routineId = result.routineId,
            routineName = routine?.name ?: result.routineName,
            completado = true,
            date = result.date,
            logId = result.resultId,
        )
        clearWorkoutSession(userId, result.routineId).getOrThrow()
    }
}
