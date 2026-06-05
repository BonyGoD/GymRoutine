package dev.bonygod.gymroutine.history.ui.interactions

sealed class HistoryEvent {
    data object OnRefresh : HistoryEvent()
}
