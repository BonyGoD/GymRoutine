package dev.bonygod.gymroutine.wear.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.CircularProgressIndicator
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.TitleCard
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import androidx.wear.compose.material3.lazy.transformedHeight
import dev.bonygod.gymroutine.wear.R
import dev.bonygod.gymroutine.wear.model.WatchRoutine
import dev.bonygod.gymroutine.wear.model.toDayToken
import dev.bonygod.gymroutine.wear.ui.interactions.WatchEvent
import dev.bonygod.gymroutine.wear.ui.interactions.WatchState
import java.time.LocalDate

private val EMPTY_STATE_HORIZONTAL_PADDING = 24.dp
private val EMPTY_STATE_SPACING = 8.dp
private val COMPLETED_GREEN = Color(0xFF1B5E20)

@Composable
fun RoutinesScreen(
    state: WatchState,
    onEvent: (WatchEvent) -> Unit,
    onNavigateToWorkout: () -> Unit,
) {
    if (state.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val workout = state.workout?.takeIf { it.hasProgress() }

    if (state.routines.isEmpty()) {
        EmptyRoutines(
            notConnected = state.phoneUnreachable,
            workoutName = workout?.routineName,
            onRetry = { onEvent(WatchEvent.OnRefresh) },
            onNavigateToWorkout = onNavigateToWorkout,
        )
        return
    }

    val listState = rememberTransformingLazyColumnState()
    val transformationSpec = rememberTransformationSpec()
    val token = LocalDate.now().dayOfWeek.toDayToken()
    val today = LocalDate.now().toString()
    val todayRoutines = state.routinesForToday(token)
    val otherRoutines = state.otherRoutines(token)

    ScreenScaffold(scrollState = listState) { contentPadding ->
        TransformingLazyColumn(state = listState, contentPadding = contentPadding) {
            if (workout != null) {
                item {
                    TitleCard(
                        onClick = onNavigateToWorkout,
                        title = { Text(stringResource(R.string.watch_in_progress), modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
                        subtitle = { Text(workout.routineName, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
                        modifier = Modifier.fillMaxWidth().transformedHeight(this, transformationSpec),
                        transformation = SurfaceTransformation(transformationSpec),
                    )
                }
            }
            if (todayRoutines.isNotEmpty()) {
                item {
                    ListHeader(
                        modifier = Modifier.fillMaxWidth().transformedHeight(this, transformationSpec),
                        transformation = SurfaceTransformation(transformationSpec),
                    ) {
                        Text(stringResource(R.string.watch_today))
                    }
                }
                items(todayRoutines, key = { it.id }) { routine ->
                    RoutineRow(
                        routine = routine,
                        hasActiveWorkout = workout != null,
                        isCompletedToday = state.isCompletedToday(routine, today),
                        onEvent = onEvent,
                        onNavigateToWorkout = onNavigateToWorkout,
                        modifier = Modifier.fillMaxWidth().transformedHeight(this, transformationSpec),
                        transformation = SurfaceTransformation(transformationSpec),
                    )
                }
            }
            if (otherRoutines.isNotEmpty()) {
                item {
                    ListHeader(
                        modifier = Modifier.fillMaxWidth().transformedHeight(this, transformationSpec),
                        transformation = SurfaceTransformation(transformationSpec),
                    ) {
                        Text(stringResource(R.string.watch_other_routines))
                    }
                }
                items(otherRoutines, key = { it.id }) { routine ->
                    RoutineRow(
                        routine = routine,
                        hasActiveWorkout = workout != null,
                        isCompletedToday = state.isCompletedToday(routine, today),
                        onEvent = onEvent,
                        onNavigateToWorkout = onNavigateToWorkout,
                        modifier = Modifier.fillMaxWidth().transformedHeight(this, transformationSpec),
                        transformation = SurfaceTransformation(transformationSpec),
                    )
                }
            }
            if (state.phoneUnreachable) {
                item {
                    Text(
                        text = stringResource(R.string.watch_phone_unreachable),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyRoutines(
    notConnected: Boolean,
    workoutName: String?,
    onRetry: () -> Unit,
    onNavigateToWorkout: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.padding(horizontal = EMPTY_STATE_HORIZONTAL_PADDING),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(EMPTY_STATE_SPACING),
        ) {
            Text(
                text = stringResource(
                    if (notConnected) R.string.watch_phone_not_connected else R.string.watch_routines_empty,
                ),
                textAlign = TextAlign.Center,
            )
            Button(onClick = onRetry) {
                Text(stringResource(R.string.watch_retry))
            }
            if (workoutName != null) {
                Button(
                    onClick = onNavigateToWorkout,
                    label = { Text(stringResource(R.string.watch_in_progress)) },
                    secondaryLabel = { Text(workoutName) },
                )
            }
        }
    }
}

@Composable
private fun RoutineRow(
    routine: WatchRoutine,
    hasActiveWorkout: Boolean,
    isCompletedToday: Boolean,
    onEvent: (WatchEvent) -> Unit,
    onNavigateToWorkout: () -> Unit,
    modifier: Modifier,
    transformation: SurfaceTransformation,
) {
    Button(
        onClick = {
            if (hasActiveWorkout) {
                onNavigateToWorkout()
            } else {
                onEvent(WatchEvent.OnStartRoutine(routine.id))
                onNavigateToWorkout()
            }
        },
        label = { Text(routine.name, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
        secondaryLabel = {
            Text(
                text = if (isCompletedToday) {
                    stringResource(R.string.watch_done_today)
                } else {
                    stringResource(R.string.watch_exercise_count, routine.exercises.size)
                },
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
        },
        enabled = !isCompletedToday,
        colors = ButtonDefaults.buttonColors(
            disabledContainerColor = COMPLETED_GREEN,
            disabledContentColor = Color.White,
            disabledSecondaryContentColor = Color.White,
        ),
        modifier = modifier,
        transformation = transformation,
    )
}
