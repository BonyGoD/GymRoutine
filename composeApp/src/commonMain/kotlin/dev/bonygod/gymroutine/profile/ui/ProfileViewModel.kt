package dev.bonygod.gymroutine.profile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bonygod.gymroutine.auth.domain.usecase.GetCurrentUserUseCase
import dev.bonygod.gymroutine.auth.domain.usecase.LogoutUseCase
import dev.bonygod.gymroutine.core.navigation.Navigator
import dev.bonygod.gymroutine.core.navigation.Routes
import dev.bonygod.gymroutine.profile.ui.interactions.ProfileEffect
import dev.bonygod.gymroutine.profile.ui.interactions.ProfileEvent
import dev.bonygod.gymroutine.profile.ui.interactions.ProfileState
import dev.bonygod.gymroutine.routines.domain.usecase.ObserveRoutinesUseCase
import dev.bonygod.gymroutine.workout.domain.model.WorkoutLog
import dev.bonygod.gymroutine.workout.domain.usecase.ObserveWorkoutLogsUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

class ProfileViewModel(
    private val navigator: Navigator,
    private val getCurrentUser: GetCurrentUserUseCase,
    private val logout: LogoutUseCase,
    private val observeWorkoutLogs: ObserveWorkoutLogsUseCase,
    private val observeRoutines: ObserveRoutinesUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state

    private val _effect = MutableSharedFlow<ProfileEffect>(replay = 1)
    val effect: SharedFlow<ProfileEffect> = _effect.asSharedFlow()

    init {
        load()
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.OnLogout -> performLogout()
        }
    }

    private fun load() {
        viewModelScope.launch {
            getCurrentUser()
                .onSuccess { user ->
                    setState { setUser(user?.name.orEmpty(), user?.email.orEmpty()) }
                    val uid = user?.uid.orEmpty()
                    if (uid.isEmpty()) return@onSuccess

                    combine(
                        observeWorkoutLogs(uid).catch { emit(emptyList()) },
                        observeRoutines(uid).catch { emit(emptyList()) },
                    ) { logs, routines ->
                        val total = logs.count { it.completado }
                        val streak = calculateStreak(logs)
                        val records = routines
                            .flatMap { it.exercises }
                            .count { it.weight > it.initialWeight || it.reps > it.initialReps }
                        Triple(total, records, streak)
                    }.collect { (total, records, streak) ->
                        setState { setStats(total, records, streak) }
                    }
                }
                .onFailure { e ->
                    _effect.emit(ProfileEffect.ShowError(e.message.orEmpty()))
                }
        }
    }

    private fun performLogout() {
        viewModelScope.launch {
            logout()
                .onSuccess { navigator.clearAndNavigateTo(Routes.Login) }
                .onFailure { e -> _effect.emit(ProfileEffect.ShowError(e.message.orEmpty())) }
        }
    }

    /** Días consecutivos con al menos un entreno completado, contando desde hoy hacia atrás. */
    private fun calculateStreak(logs: List<WorkoutLog>): Int {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val completedDates = logs.filter { it.completado }.map { it.date }.toSet()
        var streak = 0
        var checkDate = today
        while (completedDates.contains(checkDate.toString())) {
            streak++
            checkDate = checkDate.minus(1, DateTimeUnit.DAY)
        }
        return streak
    }

    private fun setState(reducer: ProfileState.() -> ProfileState) {
        _state.value = _state.value.reducer()
    }
}
