package dev.bonygod.gymroutine.routines.ui.interactions

import dev.bonygod.gymroutine.routines.domain.model.Routine

data class RoutinesState(
    val routines: List<Routine> = emptyList(),
    val isLoading: Boolean = false,
    // Distingue "todavía no se ha intentado cargar" de "cargado y la rutina no está".
    val hasLoaded: Boolean = false,
) {
    fun showLoading(show: Boolean) = copy(isLoading = show)

    fun finishLoading() = copy(isLoading = false, hasLoaded = true)

    fun setRoutines(routines: List<Routine>) = copy(routines = routines)
}
