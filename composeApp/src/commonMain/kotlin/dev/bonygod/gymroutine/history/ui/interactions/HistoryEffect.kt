package dev.bonygod.gymroutine.history.ui.interactions

sealed class HistoryEffect {
    data class ShowError(val message: String) : HistoryEffect()
}
