package dev.bonygod.gymroutine.workout.ui.interactions

sealed class WorkoutEvent {
    data class OnInit(val routineId: String) : WorkoutEvent()
    data class OnToggleExercise(val index: Int) : WorkoutEvent()
    data class OnUpdateWeight(val index: Int, val weight: String) : WorkoutEvent()
    data class OnUpdateReps(val index: Int, val reps: String) : WorkoutEvent()
    data class OnCompleteExercise(val index: Int) : WorkoutEvent()
    data class OnFinishWorkout(val routineId: String, val routineName: String) : WorkoutEvent()
    data object OnBackClick : WorkoutEvent()
}
