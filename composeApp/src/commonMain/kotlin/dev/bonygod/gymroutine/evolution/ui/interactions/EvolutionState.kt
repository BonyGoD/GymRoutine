package dev.bonygod.gymroutine.evolution.ui.interactions

import dev.bonygod.gymroutine.evolution.domain.model.ExerciseEvolution

data class EvolutionState(
    // Arranca en true: el primer frame se pinta antes de que OnInit encienda la carga,
    // y con false se vería un parpadeo del estado vacío antes de la lista.
    val isLoading: Boolean = true,
    val evolutions: List<ExerciseEvolution> = emptyList(),
    val selected: ExerciseEvolution? = null,
    val currentUserId: String = "",
) {
    fun showLoading(show: Boolean) = copy(isLoading = show)

    fun setEvolutions(evolutions: List<ExerciseEvolution>) = copy(evolutions = evolutions)

    fun select(evolution: ExerciseEvolution) = copy(selected = evolution)

    fun dismissDetail() = copy(selected = null)

    fun setCurrentUserId(userId: String) = copy(currentUserId = userId)
}
