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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bonygod.gymroutine.routines.domain.model.Exercise
import dev.bonygod.gymroutine.workout.ui.WorkoutViewModel
import dev.bonygod.gymroutine.workout.ui.interactions.WorkoutEvent
import dev.bonygod.gymroutine.workout.ui.model.ExerciseWorkoutForm
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel

private val GreenCompleted = Color(0xFF388E3C)
private val GreenCompletedBg = Color(0xFF1B5E20)

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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Entrenamiento", fontWeight = FontWeight.Bold, fontSize = 18.sp)
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
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
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
                    onToggle = { viewModel.onEvent(WorkoutEvent.OnToggleExercise(index)) },
                    onUpdateWeight = { viewModel.onEvent(WorkoutEvent.OnUpdateWeight(index, it)) },
                    onUpdateReps = { viewModel.onEvent(WorkoutEvent.OnUpdateReps(index, it)) },
                    onComplete = { viewModel.onEvent(WorkoutEvent.OnCompleteExercise(index)) },
                )
            }

            if (state.allExercisesCompleted) {
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
                            "FINALIZAR ENTRENAMIENTO",
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
    onToggle: () -> Unit,
    onUpdateWeight: (String) -> Unit,
    onUpdateReps: (String) -> Unit,
    onComplete: () -> Unit,
) {
    val colorScheme = MaterialTheme.colorScheme

    val cardBg = when {
        isCompleted -> GreenCompletedBg.copy(alpha = 0.25f)
        isExpanded -> colorScheme.surfaceVariant
        else -> colorScheme.surface
    }
    val cardBorder = when {
        isCompleted -> GreenCompleted.copy(alpha = 0.5f)
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
                if (!isCompleted) {
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
        // ── Cabecera siempre visible ──────────────────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = exercise.name,
                    color = if (isCompleted) GreenCompleted.copy(alpha = 0.9f) else colorScheme.onSurface,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = "${exercise.sets} series × ${form.reps.ifBlank { exercise.reps.toString() }} reps",
                    color = colorScheme.onSurfaceVariant,
                    fontSize = 13.sp,
                )
            }
            if (isCompleted) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = GreenCompleted,
                    modifier = Modifier.size(26.dp),
                )
            } else {
                Text(
                    text = "${form.weight.ifBlank { exercise.weight.toString() }} kg",
                    color = colorScheme.primary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }

        // ── Contenido expandible ──────────────────────────────────────────────
        AnimatedVisibility(
            visible = isExpanded && !isCompleted,
            enter = expandVertically(),
            exit = shrinkVertically(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                // Timer
                if (exercise.restSeconds > 0) {
                    RestTimer(restSeconds = exercise.restSeconds, isVisible = isExpanded)
                }

                // Campos editables
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    WorkoutField(
                        modifier = Modifier.weight(1f),
                        label = "Peso (kg)",
                        value = form.weight,
                        onValueChange = onUpdateWeight,
                        keyboardType = KeyboardType.Decimal,
                    )
                    WorkoutField(
                        modifier = Modifier.weight(1f),
                        label = "Reps",
                        value = form.reps,
                        onValueChange = onUpdateReps,
                        keyboardType = KeyboardType.Number,
                    )
                }

                // Referencia inicial
                if (exercise.initialWeight != exercise.weight || exercise.initialReps != exercise.reps) {
                    Text(
                        text = "Inicio: ${exercise.initialWeight} kg × ${exercise.initialReps} reps",
                        color = colorScheme.onSurfaceVariant,
                        fontSize = 12.sp,
                    )
                }

                // Botón terminar ejercicio
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
                        text = "TERMINAR EJERCICIO",
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

@Composable
private fun RestTimer(restSeconds: Int, isVisible: Boolean) {
    val colorScheme = MaterialTheme.colorScheme
    var isRunning by remember(isVisible) { mutableStateOf(false) }
    var timeLeft by remember(isVisible) { mutableIntStateOf(restSeconds) }

    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (timeLeft > 0) {
                delay(1000L)
                timeLeft--
            }
            isRunning = false
        }
    }

    val finished = timeLeft == 0
    val timerColor = when {
        finished -> GreenCompleted
        isRunning -> colorScheme.primary
        else -> colorScheme.onSurfaceVariant
    }

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
                    text = "Descanso",
                    color = colorScheme.onSurfaceVariant,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.8.sp,
                )
                Text(
                    text = when {
                        finished -> "¡Listo!"
                        else -> "${timeLeft}s"
                    },
                    color = timerColor,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp,
                )
            }
        }

        // Botón iniciar / en curso
        if (!finished) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(
                        if (isRunning) {
                            colorScheme.surfaceVariant
                        } else {
                            colorScheme.primary
                        },
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
                    text = if (isRunning) "En curso…" else "Iniciar",
                    color = if (isRunning) colorScheme.onSurfaceVariant else colorScheme.onPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

@Composable
private fun WorkoutField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType,
) {
    val colorScheme = MaterialTheme.colorScheme
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {
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
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
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
