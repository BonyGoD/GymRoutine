package dev.bonygod.gymroutine.workout.ui.interactions

sealed class WorkoutEvent {
    data object OnStartWorkout : WorkoutEvent()
    data class OnFinishWorkout(val routineId: String, val routineName: String) : WorkoutEvent()
    data object OnBackClick : WorkoutEvent()
}
