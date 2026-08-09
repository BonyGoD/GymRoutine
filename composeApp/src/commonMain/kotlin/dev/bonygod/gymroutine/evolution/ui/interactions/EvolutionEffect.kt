package dev.bonygod.gymroutine.evolution.ui.interactions

sealed class EvolutionEffect {
    data class ShowError(val message: String) : EvolutionEffect()
}
