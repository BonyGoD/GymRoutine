package dev.bonygod.gymroutine.wear.ui.interactions

sealed class WatchEvent {
    data object OnRefresh : WatchEvent()
    data class OnStartRoutine(val routineId: String) : WatchEvent()
    data class OnCompleteSet(val index: Int) : WatchEvent()
    data class OnToggleSkip(val index: Int) : WatchEvent()
    data object OnSkipRest : WatchEvent()
    data object OnFinishWorkout : WatchEvent()
    data object OnDiscardWorkout : WatchEvent()
}
