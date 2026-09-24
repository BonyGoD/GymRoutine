package dev.bonygod.gymroutine.wear.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.AlertDialog
import androidx.wear.compose.material3.AlertDialogDefaults
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.EdgeButton
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.OutlinedButton
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import androidx.wear.compose.material3.lazy.transformedHeight
import dev.bonygod.gymroutine.wear.R
import dev.bonygod.gymroutine.wear.ui.interactions.WatchEvent
import dev.bonygod.gymroutine.wear.ui.interactions.WatchState

@Composable
fun WorkoutScreen(
    state: WatchState,
    onEvent: (WatchEvent) -> Unit,
    onNavigateToExercise: (Int) -> Unit,
    onNavigateToRoutines: () -> Unit,
) {
    val workout = state.workout ?: return
    val listState = rememberTransformingLazyColumnState()
    val transformationSpec = rememberTransformationSpec()
    var showFinishConfirm by remember { mutableStateOf(false) }
    var showDiscardConfirm by remember { mutableStateOf(false) }

    ScreenScaffold(
        scrollState = listState,
        edgeButton = {
            EdgeButton(onClick = { showFinishConfirm = true }, enabled = !state.isSaving) {
                Text(stringResource(R.string.watch_finish))
            }
        },
    ) { contentPadding ->
        TransformingLazyColumn(state = listState, contentPadding = contentPadding) {
            item {
                ListHeader(
                    modifier = Modifier.fillMaxWidth().transformedHeight(this, transformationSpec),
                    transformation = SurfaceTransformation(transformationSpec),
                ) {
                    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = workout.routineName, textAlign = TextAlign.Center)
                        Text(
                            text = stringResource(R.string.watch_progress, workout.doneCount(), workout.exercises.size),
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
            items(workout.exercises, key = { it.index }) { exercise ->
                val secondary = when {
                    workout.isSkipped(exercise.index) -> stringResource(R.string.watch_exercise_skipped)
                    workout.isExerciseDone(exercise.index) -> stringResource(R.string.watch_exercise_done)
                    else -> stringResource(R.string.watch_sets_progress, workout.setsDone(exercise.index), exercise.sets)
                }
                Button(
                    onClick = { onNavigateToExercise(exercise.index) },
                    label = { Text(exercise.name, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
                    secondaryLabel = { Text(secondary, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
                    modifier = Modifier.fillMaxWidth().transformedHeight(this, transformationSpec),
                    transformation = SurfaceTransformation(transformationSpec),
                )
            }
            item {
                OutlinedButton(
                    onClick = { showDiscardConfirm = true },
                    modifier = Modifier.fillMaxWidth().transformedHeight(this, transformationSpec),
                    transformation = SurfaceTransformation(transformationSpec),
                ) {
                    Text(
                        text = stringResource(R.string.watch_discard),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }

    val finishMessage = stringResource(R.string.watch_finish_message, workout.doneCount(), workout.exercises.size)
    AlertDialog(
        visible = showFinishConfirm,
        onDismissRequest = { showFinishConfirm = false },
        title = { Text(stringResource(R.string.watch_finish_title)) },
        text = { Text(finishMessage) },
        confirmButton = {
            AlertDialogDefaults.ConfirmButton(
                onClick = {
                    showFinishConfirm = false
                    onEvent(WatchEvent.OnFinishWorkout)
                },
            )
        },
    )

    AlertDialog(
        visible = showDiscardConfirm,
        onDismissRequest = { showDiscardConfirm = false },
        title = { Text(stringResource(R.string.watch_discard_title)) },
        confirmButton = {
            AlertDialogDefaults.ConfirmButton(
                onClick = {
                    showDiscardConfirm = false
                    onEvent(WatchEvent.OnDiscardWorkout)
                    onNavigateToRoutines()
                },
            )
        },
    )
}
