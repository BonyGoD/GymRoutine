package dev.bonygod.gymroutine.home.ui.interactions

import dev.bonygod.gymroutine.home.domain.model.PendingWorkout

sealed class HomeEvent {
    /** La pantalla entra en composición: al volver de otra pestaña puede haber datos de perfil nuevos. */
    data object OnScreenShown : HomeEvent()
    data class OnStartWorkout(val routineId: String, val routineName: String) : HomeEvent()
    data object OnPickOtherRoutine : HomeEvent()
    data object OnDismissRoutinePicker : HomeEvent()
    data class OnRecoverWorkout(val pending: PendingWorkout) : HomeEvent()
    data class OnDismissPending(val pending: PendingWorkout) : HomeEvent()
}
