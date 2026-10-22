package dev.bonygod.gymroutine.wear.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.CircularProgressIndicator
import androidx.wear.compose.material3.EdgeButton
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.OutlinedButton
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text
import dev.bonygod.gymroutine.wear.R
import dev.bonygod.gymroutine.wear.ui.interactions.WatchEvent
import dev.bonygod.gymroutine.wear.ui.interactions.WatchState
import java.text.NumberFormat

private val REST_RING_SIZE = 96.dp

@Composable
fun ExerciseScreen(state: WatchState, index: Int, onEvent: (WatchEvent) -> Unit) {
    val workout = state.workout ?: return
    val exercise = workout.exercises.getOrNull(index) ?: return
    val rest = state.rest?.takeIf { it.exerciseIndex == index }
    val currentRest by rememberUpdatedState(rest)

    val numberFormat = remember { NumberFormat.getNumberInstance().apply { maximumFractionDigits = 2 } }
    val weightText = remember(exercise.weight) { numberFormat.format(exercise.weight.toDouble()) }

    val listState = rememberTransformingLazyColumnState()
    val content: @Composable BoxScope.(PaddingValues) -> Unit = { contentPadding ->
        TransformingLazyColumn(state = listState, contentPadding = contentPadding) {
            item {
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = exercise.name, textAlign = TextAlign.Center)
                    Text(
                        stringResource(
                            R.string.watch_set_of,
                            (workout.setsDone(index) + 1).coerceAtMost(exercise.sets),
                            exercise.sets,
                        ),
                    )
                    if (rest == null) {
                        Text(stringResource(R.string.watch_weight_reps, weightText, exercise.reps))
                    }
                }
            }
            if (rest != null) {
                item {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Box(modifier = Modifier.size(REST_RING_SIZE), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(
                                progress = {
                                    currentRest?.let { it.secondsLeft.toFloat() / it.totalSeconds.toFloat() } ?: 0f
                                },
                                modifier = Modifier.fillMaxSize(),
                            )
                            Text(
                                text = formatCountdown(rest.secondsLeft),
                                style = MaterialTheme.typography.numeralMedium,
                            )
                        }
                    }
                }
            } else {
                item {
                    val done = workout.isExerciseDone(index)
                    val skipped = workout.isSkipped(index)
                    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Button(
                            onClick = { onEvent(WatchEvent.OnCompleteSet(index)) },
                            enabled = !done && !skipped,
                        ) {
                            Text(stringResource(R.string.watch_set_done))
                        }
                        OutlinedButton(onClick = { onEvent(WatchEvent.OnToggleSkip(index)) }) {
                            Text(stringResource(if (skipped) R.string.watch_undo_skip else R.string.watch_skip))
                        }
                    }
                }
            }
        }
    }

    if (rest != null) {
        ScreenScaffold(
            scrollState = listState,
            edgeButton = {
                EdgeButton(onClick = { onEvent(WatchEvent.OnSkipRest) }) {
                    Text(stringResource(R.string.watch_skip_rest))
                }
            },
            content = content,
        )
    } else {
        ScreenScaffold(scrollState = listState, content = content)
    }
}

private fun formatCountdown(totalSeconds: Int): String {
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%d:%02d".format(minutes, seconds)
}
