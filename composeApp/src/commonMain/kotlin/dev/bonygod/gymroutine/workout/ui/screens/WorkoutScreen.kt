package dev.bonygod.gymroutine.workout.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.bonygod.gymroutine.workout.ui.WorkoutViewModel
import dev.bonygod.gymroutine.workout.ui.interactions.WorkoutEvent
import org.koin.compose.viewmodel.koinViewModel

data class ExerciseSet(val exercise: String, val sets: Int, val reps: Int, val weight: Float)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutScreen(
    routineId: String = "",
    routineName: String = "",
    viewModel: WorkoutViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()

    // Si viene una rutina real del navegador se usa su nombre; si no, el mock
    val displayName = routineName.ifBlank { "Empuje A" }

    val exercises = listOf(
        ExerciseSet("Press banca", 4, 8, 80f),
        ExerciseSet("Press inclinado mancuerna", 3, 10, 30f),
        ExerciseSet("Press hombro", 3, 10, 50f),
        ExerciseSet("Jalón tríceps", 3, 12, 25f),
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Entrenamiento", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.onEvent(WorkoutEvent.OnBackClick) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
        ) {
            Text(
                displayName,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(16.dp))

            if (!state.isStarted) {
                Button(
                    onClick = { viewModel.onEvent(WorkoutEvent.OnStartWorkout) },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Empezar entrenamiento")
                }
            } else {
                Button(
                    onClick = {
                        viewModel.onEvent(WorkoutEvent.OnFinishWorkout(routineId, displayName))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                ) { Text("Finalizar entrenamiento") }
            }

            Spacer(Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(exercises) { ex ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        ),
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column(Modifier.weight(1f)) {
                                Text(
                                    ex.exercise,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                )
                                Text(
                                    "${ex.sets} series x ${ex.reps} reps",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                            Text(
                                "${ex.weight} kg",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }
                }
            }
        }
    }
}
