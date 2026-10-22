package dev.bonygod.gymroutine.wear.ui

import android.app.Application
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dev.bonygod.gymroutine.wear.data.PhoneSync
import dev.bonygod.gymroutine.wear.data.WorkoutStore
import dev.bonygod.gymroutine.wear.data.mapper.toResultDto
import dev.bonygod.gymroutine.wear.model.ActiveRest
import dev.bonygod.gymroutine.wear.model.ActiveWorkout
import dev.bonygod.gymroutine.wear.model.WatchRoutine
import dev.bonygod.gymroutine.wear.ui.interactions.WatchEffect
import dev.bonygod.gymroutine.wear.ui.interactions.WatchEvent
import dev.bonygod.gymroutine.wear.ui.interactions.WatchState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID

private const val REST_TICK_MILLIS = 1_000L
private const val LOADING_TIMEOUT_MILLIS = 5_000L
private val VIBRATION_PATTERN = longArrayOf(0, 300, 150, 300)

class WatchViewModel(application: Application) : AndroidViewModel(application) {

    private val phoneSync = PhoneSync(application)
    private val store = WorkoutStore(application)

    private val _state = MutableStateFlow(WatchState())
    val state: StateFlow<WatchState> = _state

    private val _effect = MutableSharedFlow<WatchEffect>(extraBufferCapacity = 1)
    val effect: SharedFlow<WatchEffect> = _effect.asSharedFlow()

    private var restJob: Job? = null

    init {
        store.load(today())?.let { restored -> setState { startWorkout(restored) } }
        viewModelScope.launch {
            val cached = phoneSync.cachedRoutines()
            setState { setRoutines(cached) }
            discardIfCompletedElsewhere(cached)
            val reachable = phoneSync.requestRoutines()
            setState { setPhoneUnreachable(!reachable) }
            if (cached.isEmpty() && reachable) {
                launch {
                    delay(LOADING_TIMEOUT_MILLIS)
                    setState { setLoading(false) }
                }
            } else {
                setState { setLoading(false) }
            }
        }
        viewModelScope.launch {
            phoneSync.routinesUpdates().collect { routines ->
                setState { setRoutines(routines) }
                discardIfCompletedElsewhere(routines)
                setState { setLoading(false) }
            }
        }
    }

    fun onEvent(event: WatchEvent) {
        when (event) {
            WatchEvent.OnRefresh -> onRefresh()
            is WatchEvent.OnStartRoutine -> onStartRoutine(event.routineId)
            is WatchEvent.OnCompleteSet -> onCompleteSet(event.index)
            is WatchEvent.OnToggleSkip -> onToggleSkip(event.index)
            WatchEvent.OnSkipRest -> onSkipRest()
            WatchEvent.OnFinishWorkout -> onFinishWorkout()
            WatchEvent.OnDiscardWorkout -> onDiscardWorkout()
        }
    }

    private fun onRefresh() {
        viewModelScope.launch {
            val reachable = phoneSync.requestRoutines()
            setState { setPhoneUnreachable(!reachable) }
        }
    }

    private fun onStartRoutine(routineId: String) {
        val routine = _state.value.routines.find { it.id == routineId } ?: return
        if (_state.value.isCompletedToday(routine, today())) return
        if (_state.value.workout?.hasProgress() == true) return
        val workout = ActiveWorkout(
            routineId = routine.id,
            routineName = routine.name,
            exercises = routine.exercises,
            startedOn = today(),
        )
        setState { startWorkout(workout) }
    }

    private fun onCompleteSet(index: Int) {
        val workout = _state.value.workout ?: return
        val updated = workout.completeSet(index)
        setState { updateWorkout(updated) }
        store.save(updated.takeIf { it.hasProgress() })
        if (updated.isExerciseDone(index)) {
            if (_state.value.rest?.exerciseIndex == index) stopRestTicker()
            setEffect(WatchEffect.ExerciseCompleted(index))
        } else {
            val exercise = updated.exercises.getOrNull(index)
            if (exercise != null && exercise.restSeconds > 0) {
                startRestFor(index, exercise.restSeconds)
            }
        }
    }

    private fun onToggleSkip(index: Int) {
        val workout = _state.value.workout ?: return
        val updated = workout.toggleSkip(index)
        setState { updateWorkout(updated) }
        store.save(updated.takeIf { it.hasProgress() })
        if (_state.value.rest?.exerciseIndex == index) stopRestTicker()
    }

    private fun onSkipRest() {
        stopRestTicker()
    }

    private fun onFinishWorkout() {
        val workout = _state.value.workout ?: return
        viewModelScope.launch {
            setState { setSaving(true) }
            val result = workout.toResultDto(
                resultId = UUID.randomUUID().toString(),
                date = today(),
                finishedAt = System.currentTimeMillis(),
            )
            runCatching { phoneSync.sendResult(result) }
                .onSuccess {
                    stopRestTicker()
                    store.save(null)
                    setState { clearWorkout() }
                    setState { markCompleted(workout.routineId, today()) }
                    setState { setSaving(false) }
                    setEffect(WatchEffect.WorkoutSaved)
                }
                .onFailure {
                    setState { setSaving(false) }
                    setEffect(WatchEffect.SaveFailed)
                }
        }
    }

    private fun onDiscardWorkout() {
        stopRestTicker()
        store.save(null)
        setState { clearWorkout() }
    }

    private fun discardIfCompletedElsewhere(routines: List<WatchRoutine>) {
        val workout = _state.value.workout ?: return
        val routine = routines.find { it.id == workout.routineId } ?: return
        if (routine.lastCompletedOn == today()) {
            onDiscardWorkout()
        }
    }

    private fun startRestFor(index: Int, restSeconds: Int) {
        restJob?.cancel()
        setState { startRest(ActiveRest.start(index, nowMillis(), restSeconds)) }
        restJob = viewModelScope.launch { runRestTicker() }
    }

    private suspend fun runRestTicker() {
        while (true) {
            val rest = _state.value.rest ?: return
            val now = nowMillis()
            if (rest.isFinished(now)) {
                vibrate()
                setState { clearRest() }
                return
            }
            setState { tickRest(now) }
            delay(minOf(rest.endsAtMillis - now, REST_TICK_MILLIS))
        }
    }

    private fun stopRestTicker() {
        restJob?.cancel()
        restJob = null
        setState { clearRest() }
    }

    private fun vibrate() {
        val context = getApplication<Application>()
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            context.getSystemService(VibratorManager::class.java).defaultVibrator
        } else {
            context.getSystemService(Vibrator::class.java)
        }
        vibrator.vibrate(VibrationEffect.createWaveform(VIBRATION_PATTERN, -1))
    }

    private fun today(): String = LocalDate.now().toString()

    private fun nowMillis(): Long = System.currentTimeMillis()

    private fun setState(reducer: WatchState.() -> WatchState) {
        _state.value = _state.value.reducer()
    }

    private fun setEffect(effect: WatchEffect) {
        _effect.tryEmit(effect)
    }
}
