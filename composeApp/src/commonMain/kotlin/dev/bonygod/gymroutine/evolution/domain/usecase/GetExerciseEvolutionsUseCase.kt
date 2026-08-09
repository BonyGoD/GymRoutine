package dev.bonygod.gymroutine.evolution.domain.usecase

import dev.bonygod.gymroutine.evolution.domain.model.ExerciseEvolution
import dev.bonygod.gymroutine.routines.domain.model.Exercise
import dev.bonygod.gymroutine.routines.domain.model.Routine
import dev.bonygod.gymroutine.routines.domain.usecase.GetRoutinesUseCase

class GetExerciseEvolutionsUseCase(private val getRoutinesUseCase: GetRoutinesUseCase) {

    suspend operator fun invoke(userId: String): Result<List<ExerciseEvolution>> =
        getRoutinesUseCase(userId).map { it.toExerciseEvolutions() }
}

private fun List<Routine>.toExerciseEvolutions(): List<ExerciseEvolution> =
    flatMap { routine -> routine.exercises.map { exercise -> routine to exercise } }
        .groupBy { (_, exercise) -> exercise.name.trim().lowercase() }
        .mapNotNull { (_, entries) -> entries.toEvolutionOrNull() }
        .sortedBy { it.name.lowercase() }

// Descarta grupos sin historial y ordena la progresión agregada de más antigua a más reciente.
private fun List<Pair<Routine, Exercise>>.toEvolutionOrNull(): ExerciseEvolution? {
    val history = flatMap { (_, exercise) -> exercise.history }.sortedBy { it.timestamp }
    if (history.isEmpty()) return null
    return ExerciseEvolution(
        name = first().second.name,
        routineNames = map { (routine, _) -> routine.name }.distinct(),
        history = history,
    )
}
