package dev.bonygod.gymroutine.routines.ui.interactions

sealed class RoutinesEffect {
    data class ShowError(val message: String) : RoutinesEffect()
}
