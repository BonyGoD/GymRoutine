package dev.bonygod.gymroutine.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bonygod.gymroutine.auth.domain.usecase.GetCurrentUserUseCase
import dev.bonygod.gymroutine.core.navigation.Navigator
import dev.bonygod.gymroutine.core.navigation.Routes
import dev.bonygod.gymroutine.home.ui.interactions.HomeEffect
import dev.bonygod.gymroutine.home.ui.interactions.HomeEvent
import dev.bonygod.gymroutine.home.ui.interactions.HomeState
import dev.bonygod.gymroutine.routines.domain.mapper.hasRoutineForDay
import dev.bonygod.gymroutine.routines.domain.mapper.routinesForDay
import dev.bonygod.gymroutine.routines.domain.model.Routine
import dev.bonygod.gymroutine.routines.domain.usecase.ObserveRoutinesUseCase
import dev.bonygod.gymroutine.workout.domain.model.WorkoutLog
import dev.bonygod.gymroutine.workout.domain.usecase.ObserveWorkoutLogsUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

class HomeViewModel(
    private val navigator: Navigator,
    private val getCurrentUser: GetCurrentUserUseCase,
    private val observeRoutines: ObserveRoutinesUseCase,
    private val observeWorkoutLogs: ObserveWorkoutLogsUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state

    private val _effect = MutableSharedFlow<HomeEffect>(replay = 1)
    val effect: SharedFlow<HomeEffect> = _effect.asSharedFlow()

    init {
        viewModelScope.launch {
            getCurrentUser()
                .onSuccess { user ->
                    val weightKg = user?.weight?.toFloatOrNull() ?: 70f
                    setState {
                        setUserName(user?.name.orEmpty())
                            .setUserWeight(weightKg)
                    }
                    val userId = user?.uid.orEmpty()
                    if (userId.isNotEmpty()) {
                        launch {
                            observeRoutines(userId)
                                .catch { /* keep last known value */ }
                                .collect { routines ->
                                    setState {
                                        setRoutines(routines)
                                            .setTodayKcal(calculateTodayKcal(routines, userWeightKg))
                                            .setConsistency(calculateConsistency(routines, workoutLogs))
                                    }
                                }
                        }
                        launch {
                            observeWorkoutLogs(userId)
                                .catch { /* keep last known value */ }
                                .collect { logs ->
                                    setState {
                                        setWorkoutLogs(logs)
                                            .setConsistency(calculateConsistency(routines, logs))
                                    }
                                }
                        }
                    }
                }
                .onFailure { e ->
                    setEffect(HomeEffect.ShowError(e.message.orEmpty()))
                }
        }
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnStartWorkout -> navigator.navigateTo(Routes.Workout())
        }
    }

    // ── Kcal ─────────────────────────────────────────────────────────────────

    /**
     * Estima las kcal quemadas con las rutinas asignadas a hoy.
     * Duración estimada por ejercicio: sets × (reps × 2s + descanso).
     * Fórmula: kcal ≈ peso(kg) × minutos × 0.125
     */
    private fun calculateTodayKcal(routines: List<Routine>, weightKg: Float): Int {
        val todayAbbr = todayDayAbbr()
        val todayRoutines = routines.routinesForDay(todayAbbr)
        if (todayRoutines.isEmpty()) return 0

        val totalSeconds = todayRoutines
            .flatMap { it.exercises }
            .sumOf { exercise ->
                val restPerSet = exercise.restSeconds.coerceAtLeast(60)
                exercise.sets * (exercise.reps * 2 + restPerSet)
            }
        val minutes = totalSeconds / 60f
        return (weightKg * minutes * 0.125f).toInt()
    }

    // ── Constancia ────────────────────────────────────────────────────────────

    /**
     * Calcula el porcentaje de constancia de los últimos 30 días:
     *   (días con entreno completado) / (días con rutina programada) × 100
     */
    private fun calculateConsistency(routines: List<Routine>, logs: List<WorkoutLog>): Int {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val last30Days = (0 until 30).map { daysAgo ->
            today.minus(daysAgo.toLong(), DateTimeUnit.DAY)
        }

        val scheduledDays = last30Days.count { date ->
            routines.hasRoutineForDay(date.dayOfWeek.toSpanishAbbr())
        }
        if (scheduledDays == 0) return 0

        val completedDates = logs.map { it.date }.toSet()
        val completedDays = last30Days.count { date -> date.toString() in completedDates }

        return ((completedDays.toFloat() / scheduledDays) * 100f).toInt().coerceIn(0, 100)
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private fun todayDayAbbr(): String = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date
        .dayOfWeek
        .toSpanishAbbr()

    private fun DayOfWeek.toSpanishAbbr(): String = when (this) {
        DayOfWeek.MONDAY    -> "LUN"
        DayOfWeek.TUESDAY   -> "MAR"
        DayOfWeek.WEDNESDAY -> "MIÉ"
        DayOfWeek.THURSDAY  -> "JUE"
        DayOfWeek.FRIDAY    -> "VIE"
        DayOfWeek.SATURDAY  -> "SÁB"
        DayOfWeek.SUNDAY    -> "DOM"
        else                -> ""
    }

    private fun setState(reducer: HomeState.() -> HomeState) {
        _state.value = _state.value.reducer()
    }

    private suspend fun setEffect(effect: HomeEffect) {
        _effect.emit(effect)
    }
}
