package dev.bonygod.gymroutine.workout.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bonygod.gymroutine.routines.domain.model.Exercise
import dev.bonygod.gymroutine.workout.ui.WorkoutViewModel
import dev.bonygod.gymroutine.workout.ui.interactions.WorkoutEvent
import dev.bonygod.gymroutine.workout.ui.model.ExerciseWorkoutForm
import gymroutine.composeapp.generated.resources.Res
import gymroutine.composeapp.generated.resources.common_accept
import gymroutine.composeapp.generated.resources.common_back_description
import gymroutine.composeapp.generated.resources.workout_screen_completed_sets
import gymroutine.composeapp.generated.resources.workout_screen_field_reps
import gymroutine.composeapp.generated.resources.workout_screen_field_weight_kg
import gymroutine.composeapp.generated.resources.workout_screen_finish_exercise
import gymroutine.composeapp.generated.resources.workout_screen_finish_workout
import gymroutine.composeapp.generated.resources.workout_screen_initial_values
import gymroutine.composeapp.generated.resources.workout_screen_rest_done_message
import gymroutine.composeapp.generated.resources.workout_screen_rest_done_title
import gymroutine.composeapp.generated.resources.workout_screen_rest_label
import gymroutine.composeapp.generated.resources.workout_screen_seconds_left
import gymroutine.composeapp.generated.resources.workout_screen_sets_reps
import gymroutine.composeapp.generated.resources.workout_screen_skip_exercise
import gymroutine.composeapp.generated.resources.workout_screen_timer_running
import gymroutine.composeapp.generated.resources.workout_screen_timer_start
import gymroutine.composeapp.generated.resources.workout_screen_title
import gymroutine.composeapp.generated.resources.workout_screen_unskip_exercise
import gymroutine.composeapp.generated.resources.workout_screen_weight_kg
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

private val GreenCompleted = Color(0xFF388E3C)
private val GreenCompletedBg = Color(0xFF1B5E20)
private val GreySkipped = Color(0xFF9E9E9E)
private val GreySkippedBg = Color(0xFF1C1C1C)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutScreen(
    routineId: String = "",
    routineName: String = "",
    viewModel: WorkoutViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val colorScheme = MaterialTheme.colorScheme

    LaunchedEffect(routineId) {
        viewModel.onEvent(WorkoutEvent.OnInit(routineId))
    }

    val titleText = stringResource(Res.string.workout_screen_title)
    val backDescription = stringResource(Res.string.common_back_description)
    val finishWorkoutText = stringResource(Res.string.workout_screen_finish_workout)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(titleText, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        if (routineName.isNotBlank()) {
                            Text(
                                routineName,
                                fontSize = 13.sp,
                                color = colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Normal,
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.onEvent(WorkoutEvent.OnBackClick) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = backDescription)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.background,
                ),
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item { Spacer(Modifier.height(8.dp)) }

            itemsIndexed(state.exercises) { index, exercise ->
                ExerciseCard(
                    index = index,
                    exercise = exercise,
                    form = state.exerciseForms[index] ?: ExerciseWorkoutForm(
                        weight = exercise.weight.toString(),
                        reps = exercise.reps.toString(),
                    ),
                    isExpanded = state.expandedExerciseIndex == index,
                    isCompleted = index in state.completedExercises,
                    isSkipped = index in state.skippedExercises,
                    onToggle = { viewModel.onEvent(WorkoutEvent.OnToggleExercise(index)) },
                    onUpdateWeight = { viewModel.onEvent(WorkoutEvent.OnUpdateWeight(index, it)) },
                    onUpdateReps = { viewModel.onEvent(WorkoutEvent.OnUpdateReps(index, it)) },
                    onComplete = { viewModel.onEvent(WorkoutEvent.OnCompleteExercise(index)) },
                    onToggleSkip = { viewModel.onEvent(WorkoutEvent.OnToggleSkipExercise(index)) },
                    onSaveProgress = { viewModel.onEvent(WorkoutEvent.OnSaveExerciseProgress(index)) },
                )
            }

            if (state.allExercisesResolved) {
                item {
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = {
                            viewModel.onEvent(WorkoutEvent.OnFinishWorkout(routineId, routineName))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GreenCompleted,
                        ),
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp),
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(
                            finishWorkoutText,
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp,
                        )
                    }
                    Spacer(Modifier.height(32.dp))
                }
            } else {
                item { Spacer(Modifier.height(32.dp)) }
            }
        }
    }
}

@Composable
private fun ExerciseCard(
    index: Int,
    exercise: Exercise,
    form: ExerciseWorkoutForm,
    isExpanded: Boolean,
    isCompleted: Boolean,
    isSkipped: Boolean,
    onToggle: () -> Unit,
    onUpdateWeight: (String) -> Unit,
    onUpdateReps: (String) -> Unit,
    onComplete: () -> Unit,
    onToggleSkip: () -> Unit,
    onSaveProgress: () -> Unit,
) {
    val colorScheme = MaterialTheme.colorScheme
    var completedRestSets by remember { mutableIntStateOf(0) }

    val setsRepsText = stringResource(
        Res.string.workout_screen_sets_reps,
        exercise.sets,
        form.reps.ifBlank { exercise.reps.toString() },
    )
    val weightKgText = stringResource(
        Res.string.workout_screen_weight_kg,
        form.weight.ifBlank { exercise.weight.toString() },
    )
    val completedSetsText = stringResource(
        Res.string.workout_screen_completed_sets,
        completedRestSets,
        exercise.sets,
    )
    val initialValuesText = stringResource(
        Res.string.workout_screen_initial_values,
        exercise.initialWeight.toString(),
        exercise.initialReps,
    )
    val finishExerciseText = stringResource(Res.string.workout_screen_finish_exercise)
    val fieldWeightLabel = stringResource(Res.string.workout_screen_field_weight_kg)
    val fieldRepsLabel = stringResource(Res.string.workout_screen_field_reps)
    val skipExerciseText = stringResource(Res.string.workout_screen_skip_exercise)
    val unskipExerciseText = stringResource(Res.string.workout_screen_unskip_exercise)

    val cardBg = when {
        isCompleted -> GreenCompletedBg.copy(alpha = 0.25f)
        isSkipped -> GreySkippedBg.copy(alpha = 0.3f)
        isExpanded -> colorScheme.surfaceVariant
        else -> colorScheme.surface
    }
    val cardBorder = when {
        isCompleted -> GreenCompleted.copy(alpha = 0.5f)
        isSkipped -> GreySkipped.copy(alpha = 0.3f)
        isExpanded -> colorScheme.primary.copy(alpha = 0.6f)
        else -> colorScheme.outline.copy(alpha = 0.15f)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(cardBg)
            .border(1.dp, cardBorder, RoundedCornerShape(20.dp))
            .then(
                if (!isCompleted && !isSkipped) {
                    Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onToggle,
                    )
                } else {
                    Modifier
                },
            )
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = exercise.name,
                    color = when {
                        isCompleted -> GreenCompleted.copy(alpha = 0.9f)
                        isSkipped -> GreySkipped.copy(alpha = 0.7f)
                        else -> colorScheme.onSurface
                    },
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = setsRepsText,
                    color = colorScheme.onSurfaceVariant,
                    fontSize = 13.sp,
                )
            }

            when {
                isCompleted -> {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = GreenCompleted,
                        modifier = Modifier.size(26.dp),
                    )
                }
                isSkipped -> {
                    // Tapping the grey icon un-skips the exercise
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = onToggleSkip,
                            )
                            .padding(4.dp),
                    ) {
                        Icon(
                            Icons.Default.Cancel,
                            contentDescription = unskipExerciseText,
                            tint = GreySkipped,
                            modifier = Modifier.size(26.dp),
                        )
                    }
                }
                else -> {
                    // Default: weight label + "Omitir" chip
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            text = weightKgText,
                            color = colorScheme.primary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .border(
                                    1.dp,
                                    colorScheme.outline.copy(alpha = 0.4f),
                                    RoundedCornerShape(8.dp),
                                )
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = onToggleSkip,
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = skipExerciseText,
                                color = colorScheme.onSurfaceVariant,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                letterSpacing = 0.3.sp,
                            )
                        }
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = isExpanded && !isCompleted && !isSkipped,
            enter = expandVertically(),
            exit = shrinkVertically(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = completedSetsText,
                        color = colorScheme.onSurfaceVariant,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                    )
                    LinearProgressIndicator(
                        progress = { if (exercise.sets > 0) completedRestSets / exercise.sets.toFloat() else 0f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = colorScheme.primary,
                        trackColor = colorScheme.surfaceVariant,
                        drawStopIndicator = {},
                    )
                }

                if (exercise.restSeconds > 0) {
                    RestTimer(
                        restSeconds = exercise.restSeconds,
                        isVisible = isExpanded,
                        onRestCompleted = {
                            completedRestSets = (completedRestSets + 1).coerceAtMost(exercise.sets)
                        },
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    WorkoutField(
                        modifier = Modifier.weight(1f),
                        label = fieldWeightLabel,
                        value = form.weight,
                        onValueChange = onUpdateWeight,
                        onSave = onSaveProgress,
                        keyboardType = KeyboardType.Decimal,
                    )
                    WorkoutField(
                        modifier = Modifier.weight(1f),
                        label = fieldRepsLabel,
                        value = form.reps,
                        onValueChange = onUpdateReps,
                        onSave = onSaveProgress,
                        keyboardType = KeyboardType.Number,
                    )
                }

                if (exercise.initialWeight != exercise.weight || exercise.initialReps != exercise.reps) {
                    Text(
                        text = initialValuesText,
                        color = colorScheme.onSurfaceVariant,
                        fontSize = 12.sp,
                    )
                }

                if (completedRestSets >= exercise.sets) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(CircleShape)
                            .background(colorScheme.primary)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = onComplete,
                            )
                            .padding(vertical = 14.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = finishExerciseText,
                            color = colorScheme.onPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RestTimer(
    restSeconds: Int,
    isVisible: Boolean,
    onRestCompleted: () -> Unit,
) {
    val colorScheme = MaterialTheme.colorScheme
    var isRunning by remember(isVisible) { mutableStateOf(false) }
    var timeLeft by remember(isVisible) { mutableIntStateOf(restSeconds) }
    var showDialog by remember(isVisible) { mutableStateOf(false) }

    val restDoneTitle = stringResource(Res.string.workout_screen_rest_done_title)
    val restDoneMessage = stringResource(Res.string.workout_screen_rest_done_message)
    val acceptText = stringResource(Res.string.common_accept)
    val restLabel = stringResource(Res.string.workout_screen_rest_label)
    val timerRunningText = stringResource(Res.string.workout_screen_timer_running)
    val timerStartText = stringResource(Res.string.workout_screen_timer_start)
    val secondsLeftText = stringResource(Res.string.workout_screen_seconds_left, timeLeft)

    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (timeLeft > 0) {
                delay(1000L)
                timeLeft--
            }
            isRunning = false
            onRestCompleted()
            timeLeft = restSeconds
            showDialog = true
        }
    }

    if (showDialog) {
        LaunchedEffect(showDialog) {
            delay(5000L)
            showDialog = false
        }
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    text = restDoneTitle,
                    fontWeight = FontWeight.Bold,
                )
            },
            text = {
                Text(text = restDoneMessage)
            },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(acceptText)
                }
            },
        )
    }

    val timerColor = if (isRunning) colorScheme.primary else colorScheme.onSurfaceVariant

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(colorScheme.background)
            .border(1.dp, colorScheme.outline.copy(alpha = 0.15f), RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(
                Icons.Default.Timer,
                contentDescription = null,
                tint = timerColor,
                modifier = Modifier.size(20.dp),
            )
            Column {
                Text(
                    text = restLabel,
                    color = colorScheme.onSurfaceVariant,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.8.sp,
                )
                Text(
                    text = secondsLeftText,
                    color = timerColor,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp,
                )
            }
        }

        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(
                    if (isRunning) colorScheme.surfaceVariant else colorScheme.primary,
                )
                .then(
                    if (!isRunning) {
                        Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) { isRunning = true }
                    } else {
                        Modifier
                    },
                )
                .padding(horizontal = 14.dp, vertical = 8.dp),
        ) {
            Text(
                text = if (isRunning) timerRunningText else timerStartText,
                color = if (isRunning) colorScheme.onSurfaceVariant else Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@Composable
private fun WorkoutField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    onSave: () -> Unit,
    keyboardType: KeyboardType,
) {
    val colorScheme = MaterialTheme.colorScheme
    // Guard: only trigger save on blur if the field was actually focused first,
    // to avoid spurious saves on initial composition.
    var wasFocused by remember { mutableStateOf(false) }

    // onFocusChanged is applied to the Column so that focus propagation from the
    // inner BasicTextField bubbles up correctly without interfering with the
    // composable-lambda inference of BasicTextField's own parameters.
    Column(
        modifier = modifier.onFocusChanged { focusState ->
            if (focusState.hasFocus) {
                wasFocused = true
            } else if (wasFocused) {
                onSave()
            }
        },
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            text = label,
            color = colorScheme.onSurfaceVariant,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.8.sp,
        )
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(
                onDone = { onSave() },
            ),
            textStyle = TextStyle(
                color = colorScheme.onSurface,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(colorScheme.background)
                        .border(1.dp, colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                ) {
                    innerTextField()
                }
            },
        )
    }
}
