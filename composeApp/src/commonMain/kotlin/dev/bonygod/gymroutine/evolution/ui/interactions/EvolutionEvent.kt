package dev.bonygod.gymroutine.evolution.ui.interactions

import dev.bonygod.gymroutine.evolution.domain.model.ExerciseEvolution

sealed class EvolutionEvent {
    data object OnInit : EvolutionEvent()

    data class OnSelectExercise(val evolution: ExerciseEvolution) : EvolutionEvent()

    data object OnDismissDetail : EvolutionEvent()

    data object OnSeedTestData : EvolutionEvent()
}
