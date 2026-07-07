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
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
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
                                        val records = calculateWeekRecords(routines, workoutLogs)
                                        setRoutines(routines)
                                            .setTodayKcal(calculateTodayKcal(routines, workoutLogs, userWeightKg))
                                            .setConsistency(calculateConsistency(routines, workoutLogs))
                                            .setIsTodayCompleted(calculateIsTodayCompleted(workoutLogs))
                                            .setWeekRecords(records.first, records.second)
                                    }
                                }
                        }
                        launch {
                            observeWorkoutLogs(userId)
                                .catch { /* keep last known value */ }
                                .collect { logs ->
                                    setState {
                                        val records = calculateWeekRecords(routines, logs)
                                        setWorkoutLogs(logs)
                                            .setTodayKcal(calculateTodayKcal(routines, logs, userWeightKg))
                                            .setConsistency(calculateConsistency(routines, logs))
                                            .setIsTodayCompleted(calculateIsTodayCompleted(logs))
                                            .setWeekRecords(records.first, records.second)
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
            is HomeEvent.OnStartWorkout -> navigator.navigateTo(
                Routes.Workout(routineId = event.routineId, routineName = event.routineName),
            )
        }
    }

    // ── Kcal ─────────────────────────────────────────────────────────────────

    /**
     * Solo muestra kcal si el entreno de hoy está marcado como completado.
     * Fórmula: kcal ≈ peso(kg) × minutos × 0.125
     */
    private fun calculateTodayKcal(routines: List<Routine>, logs: List<WorkoutLog>, weightKg: Float): Int {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.toString()
        if (logs.none { it.date == today && it.completado }) return 0

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
     * Cuenta entrenos con completado = true en los 7 días de la semana en curso (lun–dom).
     */
    private fun calculateConsistency(routines: List<Routine>, logs: List<WorkoutLog>): Int {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        // Lunes de la semana actual (dayOfWeek.ordinal: MONDAY=0 … SUNDAY=6)
        val mondayOfWeek = today.minus(today.dayOfWeek.ordinal.toLong(), DateTimeUnit.DAY)
        val currentWeekDays = (0 until 7).map { offset ->
            mondayOfWeek.plus(offset.toLong(), DateTimeUnit.DAY)
        }

        val scheduledDays = currentWeekDays.count { date ->
            routines.hasRoutineForDay(date.dayOfWeek.toSpanishAbbr())
        }
        if (scheduledDays == 0) return 0

        val completedDates = logs.filter { it.completado }.map { it.date }.toSet()
        val completedDays = currentWeekDays.count { date -> date.toString() in completedDates }

        return ((completedDays.toFloat() / scheduledDays) * 100f).toInt().coerceIn(0, 100)
    }

    // ── Completado hoy ────────────────────────────────────────────────────────

    private fun calculateIsTodayCompleted(logs: List<WorkoutLog>): Boolean {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.toString()
        return logs.any { it.date == today && it.completado }
    }

    // ── Récords semanales ─────────────────────────────────────────────────────

    /**
     * Busca ejercicios con mejora de peso o repeticiones respecto al valor inicial,
     * limitado a las rutinas entrenadas (completadas) en los últimos 7 días.
     * Devuelve (count, subtítulo con los nombres).
     */
    private fun calculateWeekRecords(routines: List<Routine>, logs: List<WorkoutLog>): Pair<Int, String> {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val weekAgo = today.minus(7, DateTimeUnit.DAY)

        val weekRoutineIds = logs
            .filter { it.completado }
            .filter {
                runCatching { LocalDate.parse(it.date) >= weekAgo }.getOrDefault(false)
            }
            .map { it.routineId }
            .toSet()

        if (weekRoutineIds.isEmpty()) return Pair(0, "Sin entrenos esta semana")

        val recordExercises = routines
            .filter { it.id in weekRoutineIds }
            .flatMap { it.exercises }
            .filter { it.weight > it.initialWeight || it.reps > it.initialReps }

        if (recordExercises.isEmpty()) return Pair(0, "Sin nuevos récords")

        val subtitle = recordExercises.take(3).joinToString(", ") { it.name }
        return Pair(recordExercises.size, subtitle)
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private fun todayDayAbbr(): String = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date
        .dayOfWeek
        .toSpanishAbbr()

    private fun DayOfWeek.toSpanishAbbr(): String = when (this) {
        DayOfWeek.MONDAY -> "LUN"
        DayOfWeek.TUESDAY -> "MAR"
        DayOfWeek.WEDNESDAY -> "MIÉ"
        DayOfWeek.THURSDAY -> "JUE"
        DayOfWeek.FRIDAY -> "VIE"
        DayOfWeek.SATURDAY -> "SÁB"
        DayOfWeek.SUNDAY -> "DOM"
        else -> ""
    }

    private fun setState(reducer: HomeState.() -> HomeState) {
        _state.value = _state.value.reducer()
    }

    private suspend fun setEffect(effect: HomeEffect) {
        _effect.emit(effect)
    }
}
