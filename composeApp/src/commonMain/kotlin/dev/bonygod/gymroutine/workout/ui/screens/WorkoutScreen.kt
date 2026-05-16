package dev.bonygod.gymroutine.workout.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class ExerciseSet(val exercise: String, val sets: Int, val reps: Int, val weight: Float)

@Composable
fun WorkoutScreen() {
    var started by remember { mutableStateOf(false) }
    val exercises = listOf(
        ExerciseSet("Press banca", 4, 8, 80f),
        ExerciseSet("Press inclinado mancuerna", 3, 10, 30f),
        ExerciseSet("Press hombro", 3, 10, 50f),
        ExerciseSet("Jalon triceps", 3, 12, 25f),
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Entrenamiento de hoy", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
        Spacer(Modifier.height(4.dp))
        Text("Empuje A", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(16.dp))

        if (!started) {
            Button(onClick = { started = true }, modifier = Modifier.fillMaxWidth()) {
                Text("Empezar entrenamiento")
            }
        } else {
            Button(
                onClick = { started = false },
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
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text(ex.exercise, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                            Text("${ex.sets} series x ${ex.reps} reps", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Text("${ex.weight} kg", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}
