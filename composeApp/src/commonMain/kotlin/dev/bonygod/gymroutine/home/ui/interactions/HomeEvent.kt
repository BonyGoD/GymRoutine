package dev.bonygod.gymroutine.home.ui.interactions

sealed class HomeEvent {
    data class OnStartWorkout(val routineId: String, val routineName: String) : HomeEvent()
}
