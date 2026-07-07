package dev.bonygod.gymroutine.routines.ui.interactions

import dev.bonygod.gymroutine.routines.domain.model.Routine

data class RoutinesState(
    val routines: List<Routine> = emptyList(),
    val isLoading: Boolean = false,
) {
    fun showLoading(show: Boolean) = copy(isLoading = show)

    fun setRoutines(routines: List<Routine>) = copy(routines = routines)
}
