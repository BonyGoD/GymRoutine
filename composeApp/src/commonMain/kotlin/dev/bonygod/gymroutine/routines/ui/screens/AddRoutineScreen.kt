package dev.bonygod.gymroutine.routines.ui.screens

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bonygod.gymroutine.core.navigation.Navigator
import dev.bonygod.gymroutine.routines.domain.model.Exercise
import dev.bonygod.gymroutine.routines.domain.model.Routine
import dev.bonygod.gymroutine.routines.ui.RoutinesUiState
import dev.bonygod.gymroutine.routines.ui.RoutinesViewModel
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

// ── Estado interno del formulario de ejercicio ────────────────────────────────

private data class ExerciseForm(
    val name: String = "",
    val sets: String = "",
    val reps: String = "",
    val weight: String = "",
    val restSeconds: String = "",
    val days: String = "",
)

private fun ExerciseForm.toExercise() = Exercise(
    name = name,
    sets = sets.toIntOrNull() ?: 0,
    reps = reps.toIntOrNull() ?: 0,
    weight = weight.toFloatOrNull() ?: 0f,
    restSeconds = restSeconds.toIntOrNull() ?: 0,
    days = days,
)

private fun Exercise.toForm() = ExerciseForm(
    name = name,
    sets = if (sets == 0) "" else sets.toString(),
    reps = if (reps == 0) "" else reps.toString(),
    weight = if (weight == 0f) "" else weight.toString(),
    restSeconds = if (restSeconds == 0) "" else restSeconds.toString(),
    days = days,
)

// ── Pantalla ──────────────────────────────────────────────────────────────────

@Composable
fun AddRoutineScreen(
    routineId: String? = null,
    navigator: Navigator = koinInject(),
    viewModel: RoutinesViewModel = koinViewModel(),
) {
    val colorScheme = MaterialTheme.colorScheme
    val uiState by viewModel.uiState.collectAsState()

    val existing = routineId?.let {
        (uiState as? RoutinesUiState.Success)?.routines?.firstOrNull { it.id == routineId }
    }

    val isEdit = routineId != null

    var routineName by remember(existing) { mutableStateOf(existing?.name ?: "") }
    val exercises = remember(existing) {
        mutableStateListOf<ExerciseForm>().also { list ->
            existing?.exercises?.forEach { list.add(it.toForm()) }
        }
    }
    var showExerciseForm by remember { mutableStateOf(false) }
    var draft by remember { mutableStateOf(ExerciseForm()) }
    var editingIndex by remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background),
    ) {
        // ── Header ────────────────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 56.dp, start = 16.dp, end = 24.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(colorScheme.surfaceVariant)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) { navigator.goBack() },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = colorScheme.onSurface,
                    modifier = Modifier.size(20.dp),
                )
            }
            Text(
                text = if (isEdit) "Editar Rutina" else "Nueva Rutina",
                color = colorScheme.primary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.5).sp,
            )
        }

        // ── Contenido scrollable ──────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(start = 24.dp, end = 24.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            // ── Nombre de la rutina ───────────────────────────────────────────
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "NOMBRE",
                    color = colorScheme.onSurfaceVariant,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp,
                )
                OutlinedTextField(
                    value = routineName,
                    onValueChange = { routineName = it },
                    placeholder = {
                        Text(
                            text = "Ej: Push Day",
                            color = colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorScheme.primary,
                        unfocusedBorderColor = colorScheme.outline.copy(alpha = 0.3f),
                        focusedTextColor = colorScheme.onSurface,
                        unfocusedTextColor = colorScheme.onSurface,
                        cursorColor = colorScheme.primary,
                        focusedContainerColor = colorScheme.surface,
                        unfocusedContainerColor = colorScheme.surface,
                    ),
                )
            }

            // ── Lista de ejercicios añadidos ──────────────────────────────────
            if (exercises.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "EJERCICIOS",
                        color = colorScheme.onSurfaceVariant,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.sp,
                    )
                    exercises.forEachIndexed { index, exercise ->
                        ExerciseItem(
                            exercise = exercise,
                            onEdit = {
                                editingIndex = index
                                draft = exercise
                                showExerciseForm = true
                            },
                            onDelete = { exercises.removeAt(index) },
                        )
                    }
                }
            }

            // ── Formulario nuevo ejercicio (expandible) ───────────────────────
            AnimatedVisibility(
                visible = showExerciseForm,
                enter = expandVertically(),
                exit = shrinkVertically(),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(colorScheme.surface)
                        .border(1.dp, colorScheme.outline.copy(alpha = 0.15f), RoundedCornerShape(16.dp))
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text(
                        text = if (editingIndex != null) "EDITAR EJERCICIO" else "NUEVO EJERCICIO",
                        color = colorScheme.primary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.sp,
                    )

                    FormField(
                        label = "Nombre",
                        value = draft.name,
                        placeholder = "Ej: Press Banca",
                        onValueChange = { draft = draft.copy(name = it) },
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        FormField(
                            label = "Series",
                            value = draft.sets,
                            placeholder = "4",
                            onValueChange = { draft = draft.copy(sets = it) },
                            keyboard = KeyboardType.Number,
                            modifier = Modifier.weight(1f),
                        )
                        FormField(
                            label = "Reps",
                            value = draft.reps,
                            placeholder = "10",
                            onValueChange = { draft = draft.copy(reps = it) },
                            keyboard = KeyboardType.Number,
                            modifier = Modifier.weight(1f),
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        FormField(
                            label = "Peso (kg)",
                            value = draft.weight,
                            placeholder = "80",
                            onValueChange = { draft = draft.copy(weight = it) },
                            keyboard = KeyboardType.Decimal,
                            modifier = Modifier.weight(1f),
                        )
                        FormField(
                            label = "Descanso (s)",
                            value = draft.restSeconds,
                            placeholder = "90",
                            onValueChange = { draft = draft.copy(restSeconds = it) },
                            keyboard = KeyboardType.Number,
                            modifier = Modifier.weight(1f),
                        )
                    }
                    FormField(
                        label = "Días",
                        value = draft.days,
                        placeholder = "Ej: Lun, Jue",
                        onValueChange = { draft = draft.copy(days = it) },
                    )

                    // Acciones del formulario
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(colorScheme.surfaceVariant)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                ) {
                                    showExerciseForm = false
                                    editingIndex = null
                                    draft = ExerciseForm()
                                }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = "Cancelar",
                                color = colorScheme.onSurfaceVariant,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                            )
                        }
                        val canAdd = draft.name.isNotBlank()
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (canAdd) colorScheme.primary else colorScheme.surfaceVariant)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    enabled = canAdd,
                                ) {
                                    val idx = editingIndex
                                    if (idx != null) {
                                        exercises[idx] = draft
                                    } else {
                                        exercises.add(draft)
                                    }
                                    editingIndex = null
                                    draft = ExerciseForm()
                                    showExerciseForm = false
                                }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = if (editingIndex != null) "Guardar" else "Añadir",
                                color = if (canAdd) colorScheme.onPrimary else colorScheme.onSurfaceVariant,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }
                }
            }

            // ── Botón añadir ejercicio ────────────────────────────────────────
            if (!showExerciseForm) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) { showExerciseForm = true }
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Default.FitnessCenter,
                            contentDescription = null,
                            tint = colorScheme.primary,
                            modifier = Modifier.size(18.dp),
                        )
                        Text(
                            text = "Añadir ejercicio",
                            color = colorScheme.primary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }
            }

            // ── Guardar rutina ────────────────────────────────────────────────
            val canSave = routineName.isNotBlank() && exercises.isNotEmpty()
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (canSave) colorScheme.primary else colorScheme.surfaceVariant)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        enabled = canSave,
                    ) {
                        val routine = Routine(
                            id = existing?.id ?: "",
                            name = routineName,
                            exercises = exercises.map { it.toExercise() },
                        )
                        if (isEdit) {
                            viewModel.onUpdateRoutine(routine)
                        } else {
                            viewModel.onCreateRoutine(routine)
                        }
                        navigator.goBack()
                    }
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = if (isEdit) "Guardar Cambios" else "Guardar Rutina",
                    color = if (canSave) colorScheme.onPrimary else colorScheme.onSurfaceVariant,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

// ── Componentes ───────────────────────────────────────────────────────────────

@Composable
private fun ExerciseItem(exercise: ExerciseForm, onEdit: () -> Unit, onDelete: () -> Unit) {
    val colorScheme = MaterialTheme.colorScheme
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(colorScheme.surface)
            .border(1.dp, colorScheme.outline.copy(alpha = 0.12f), RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(3.dp),
        ) {
            Text(
                text = exercise.name,
                color = colorScheme.onSurface,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
            )
            val meta = buildList {
                if (exercise.sets.isNotBlank()) add("${exercise.sets} series")
                if (exercise.reps.isNotBlank()) add("${exercise.reps} reps")
                if (exercise.weight.isNotBlank()) add("${exercise.weight} kg")
                if (exercise.days.isNotBlank()) add(exercise.days)
            }.joinToString(" · ")
            if (meta.isNotBlank()) {
                Text(
                    text = meta,
                    color = colorScheme.onSurfaceVariant,
                    fontSize = 13.sp,
                )
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(colorScheme.surfaceVariant)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) { onEdit() },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar",
                    tint = colorScheme.primary,
                    modifier = Modifier.size(16.dp),
                )
            }
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(colorScheme.surfaceVariant)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) { onDelete() },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp),
                )
            }
        }
    }
}

@Composable
private fun FormField(
    label: String,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    keyboard: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier.fillMaxWidth(),
) {
    val colorScheme = MaterialTheme.colorScheme
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = label,
            color = colorScheme.onSurfaceVariant,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    color = colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                    fontSize = 14.sp,
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            keyboardOptions = KeyboardOptions(keyboardType = keyboard),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorScheme.primary,
                unfocusedBorderColor = colorScheme.outline.copy(alpha = 0.25f),
                focusedTextColor = colorScheme.onSurface,
                unfocusedTextColor = colorScheme.onSurface,
                cursorColor = colorScheme.primary,
                focusedContainerColor = colorScheme.background,
                unfocusedContainerColor = colorScheme.background,
            ),
        )
    }
}
