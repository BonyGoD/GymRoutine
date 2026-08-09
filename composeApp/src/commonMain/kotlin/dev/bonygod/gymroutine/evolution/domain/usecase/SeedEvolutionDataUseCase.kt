package dev.bonygod.gymroutine.evolution.domain.usecase

import dev.bonygod.gymroutine.routines.domain.model.Exercise
import dev.bonygod.gymroutine.routines.domain.model.ExerciseProgress
import dev.bonygod.gymroutine.routines.domain.usecase.GetRoutinesUseCase
import dev.bonygod.gymroutine.routines.domain.usecase.UpdateRoutineUseCase
import kotlin.time.Clock

private const val SEED_RECORD_COUNT = 8

// Al primer ejercicio sembrado se le da una serie larga, para poder ver cómo se comporta la
// gráfica (scroll horizontal, densidad de etiquetas) con un historial grande.
private const val LONG_SEED_RECORD_COUNT = 50
private const val MIN_SEED_WEIGHT_KG = 2.5f
private const val SEED_WEIGHT_STEP_KG = 2.5f
private const val SEED_MIN_REPS = 8
private const val SEED_REPS_OSCILLATION = 5
private const val WEEK_MILLIS = 7L * 24 * 60 * 60 * 1000

/**
 * Rellena de progresión sintética los ejercicios que aún no la tienen (`history.size <= 1`), para
 * poder validar la gráfica de evolución sin depender de credenciales de Firebase fuera de la app.
 * Los ejercicios con progresión real (`history.size >= 2`) se dejan intactos.
 */
class SeedEvolutionDataUseCase(
    private val getRoutines: GetRoutinesUseCase,
    private val updateRoutine: UpdateRoutineUseCase,
) {
    suspend operator fun invoke(userId: String): Result<Unit> {
        val routines = getRoutines(userId).getOrElse { return Result.failure(it) }
        // El primer ejercicio que se siembre se lleva la serie larga; el resto, la corta.
        var longSeriesPending = true
        routines.forEach { routine ->
            val seededExercises = routine.exercises.map { exercise ->
                if (exercise.history.size > 1) {
                    exercise
                } else {
                    val recordCount = if (longSeriesPending) LONG_SEED_RECORD_COUNT else SEED_RECORD_COUNT
                    longSeriesPending = false
                    exercise.copy(history = seedHistory(exercise, recordCount))
                }
            }
            if (seededExercises != routine.exercises) {
                updateRoutine(userId, routine.copy(exercises = seededExercises)).getOrElse { return Result.failure(it) }
            }
        }
        return Result.success(Unit)
    }

    /**
     * Genera [recordCount] registros retrocediendo desde el peso/reps actuales del ejercicio
     * (el último punto). El peso baja [SEED_WEIGHT_STEP_KG] cada `recordsPerWeightStep` registros
     * hacia atrás sin bajar de [MIN_SEED_WEIGHT_KG]; las reps oscilan entre [SEED_MIN_REPS] y
     * [SEED_MIN_REPS] + 4, salvo el último punto, que conserva las reps reales del ejercicio.
     * Los timestamps retroceden una semana por registro desde ahora.
     */
    private fun seedHistory(exercise: Exercise, recordCount: Int): List<ExerciseProgress> {
        val now = Clock.System.now().toEpochMilliseconds()
        val currentWeight = exercise.weight
        val currentReps = exercise.reps
        // Cuántos registros comparten peso. Se escala con la longitud de la serie para que una
        // serie larga no toque el suelo de MIN_SEED_WEIGHT_KG y se quede plana en la mitad izquierda.
        val recordsPerWeightStep = (recordCount / 12).coerceAtLeast(2)
        return (0 until recordCount).map { index ->
            val stepsBack = (recordCount - 1) - index
            val weightSteps = stepsBack / recordsPerWeightStep
            val weight = (currentWeight - SEED_WEIGHT_STEP_KG * weightSteps).coerceAtLeast(MIN_SEED_WEIGHT_KG)
            val reps = if (stepsBack == 0) currentReps else SEED_MIN_REPS + (stepsBack % SEED_REPS_OSCILLATION)
            ExerciseProgress(weight = weight, reps = reps, timestamp = now - stepsBack * WEEK_MILLIS)
        }
    }
}
