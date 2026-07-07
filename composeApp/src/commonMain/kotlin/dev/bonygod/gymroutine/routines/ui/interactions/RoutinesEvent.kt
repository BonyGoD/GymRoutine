package dev.bonygod.gymroutine.routines.ui.interactions

import dev.bonygod.gymroutine.routines.domain.model.Routine

sealed class RoutinesEvent {
    data object OnLoadRoutines : RoutinesEvent()

    data class OnCreateRoutine(val routine: Routine) : RoutinesEvent()

    data class OnUpdateRoutine(val routine: Routine) : RoutinesEvent()

    data class OnDeleteRoutine(val routineId: String) : RoutinesEvent()

    data object OnNavigateToAddRoutine : RoutinesEvent()

    data class OnNavigateToEditRoutine(val routineId: String) : RoutinesEvent()

    data object OnBackClick : RoutinesEvent()
}
