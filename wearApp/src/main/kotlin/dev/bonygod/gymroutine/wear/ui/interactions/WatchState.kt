package dev.bonygod.gymroutine.wear.ui.interactions

import dev.bonygod.gymroutine.wear.model.ActiveRest
import dev.bonygod.gymroutine.wear.model.ActiveWorkout
import dev.bonygod.gymroutine.wear.model.WatchRoutine

data class WatchState(
    val isLoading: Boolean = true,
    val routines: List<WatchRoutine> = emptyList(),
    val phoneUnreachable: Boolean = false,
    val workout: ActiveWorkout? = null,
    val rest: ActiveRest? = null,
    val isSaving: Boolean = false,
) {
    fun setRoutines(routines: List<WatchRoutine>) = copy(routines = routines)

    fun setLoading(loading: Boolean) = copy(isLoading = loading)

    fun setPhoneUnreachable(unreachable: Boolean) = copy(phoneUnreachable = unreachable)

    fun startWorkout(workout: ActiveWorkout) = copy(workout = workout)

    fun updateWorkout(workout: ActiveWorkout) = copy(workout = workout)

    fun startRest(rest: ActiveRest) = copy(rest = rest)

    fun tickRest(nowMillis: Long) = copy(rest = rest?.tick(nowMillis))

    fun clearRest() = copy(rest = null)

    fun setSaving(saving: Boolean) = copy(isSaving = saving)

    fun clearWorkout() = copy(workout = null, rest = null)

    fun markCompleted(routineId: String, date: String) = copy(
        routines = routines.map { if (it.id == routineId) it.copy(lastCompletedOn = date) else it },
    )

    fun isCompletedToday(routine: WatchRoutine, today: String): Boolean = routine.lastCompletedOn == today

    fun routinesForToday(token: String) = routines.filter { token in it.days }

    fun otherRoutines(token: String) = routines.filterNot { token in it.days }
}
