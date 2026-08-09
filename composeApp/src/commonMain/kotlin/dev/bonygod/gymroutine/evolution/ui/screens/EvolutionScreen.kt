package dev.bonygod.gymroutine.evolution.ui.screens

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import dev.bonygod.gymroutine.evolution.domain.model.ExerciseEvolution
import dev.bonygod.gymroutine.evolution.ui.EvolutionViewModel
import dev.bonygod.gymroutine.evolution.ui.components.EvolutionChart
import dev.bonygod.gymroutine.evolution.ui.interactions.EvolutionEffect
import dev.bonygod.gymroutine.evolution.ui.interactions.EvolutionEvent
import gymroutine.composeapp.generated.resources.Res
import gymroutine.composeapp.generated.resources.evolution_detail_close
import gymroutine.composeapp.generated.resources.evolution_detail_reps_label
import gymroutine.composeapp.generated.resources.evolution_detail_reps_value
import gymroutine.composeapp.generated.resources.evolution_detail_weight_label
import gymroutine.composeapp.generated.resources.evolution_detail_weight_value
import gymroutine.composeapp.generated.resources.evolution_screen_empty
import gymroutine.composeapp.generated.resources.evolution_screen_seed_test_data
import gymroutine.composeapp.generated.resources.evolution_screen_session_count
import gymroutine.composeapp.generated.resources.evolution_screen_title
import gymroutine.composeapp.generated.resources.evolution_screen_weight_delta
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

// TEMPORAL: datos de prueba para validar la gráfica. Quitar antes de publicar.
private const val DEBUG_SEED_UID = "sNxgAQJ5iTbI7qaoU53DPVw5SW22"

@Composable
fun EvolutionScreen(
    vmKey: String = "",
    viewModel: EvolutionViewModel = koinViewModel(key = vmKey.ifBlank { null }),
) {
    val colorScheme = MaterialTheme.colorScheme
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.onEvent(EvolutionEvent.OnInit)
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is EvolutionEffect.ShowError -> snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorScheme.background),
        ) {
            // ── Header fijo ───────────────────────────────────────────────────
            Text(
                text = stringResource(Res.string.evolution_screen_title),
                color = colorScheme.primary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.56).sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 96.dp, start = 24.dp, end = 24.dp, bottom = 16.dp),
            )

            // ── Contenido ─────────────────────────────────────────────────────
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                }

                state.evolutions.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
                    ) {
                        Text(
                            text = stringResource(Res.string.evolution_screen_empty),
                            color = colorScheme.onSurfaceVariant,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                        )
                        // Sin esto, el botón que puebla los datos quedaría oculto justo cuando no hay datos.
                        if (state.currentUserId == DEBUG_SEED_UID) {
                            SeedTestDataButton(onClick = { viewModel.onEvent(EvolutionEvent.OnSeedTestData) })
                        }
                    }
                }

                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .padding(start = 24.dp, end = 24.dp, bottom = 88.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        state.evolutions.forEach { evolution ->
                            ExerciseEvolutionCard(
                                evolution = evolution,
                                onClick = { viewModel.onEvent(EvolutionEvent.OnSelectExercise(evolution)) },
                            )
                        }
                        if (state.currentUserId == DEBUG_SEED_UID) {
                            SeedTestDataButton(onClick = { viewModel.onEvent(EvolutionEvent.OnSeedTestData) })
                        }
                    }
                }
            }
        }

        val selected = state.selected
        if (selected != null) {
            EvolutionDetailDialog(
                evolution = selected,
                onDismiss = { viewModel.onEvent(EvolutionEvent.OnDismissDetail) },
            )
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter),
        ) { data ->
            Snackbar(
                snackbarData = data,
                containerColor = colorScheme.surfaceVariant,
                contentColor = colorScheme.onSurface,
            )
        }
    }
}

@Composable
private fun ExerciseEvolutionCard(evolution: ExerciseEvolution, onClick: () -> Unit) {
    val colorScheme = MaterialTheme.colorScheme
    val delta = evolution.weightDelta
    val deltaText = if (delta > 0) "+$delta" else "$delta"
    val deltaColor = if (delta > 0) colorScheme.primary else colorScheme.onSurfaceVariant

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(colorScheme.surface)
            .border(1.dp, colorScheme.outline.copy(alpha = 0.15f), RoundedCornerShape(20.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) { onClick() }
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = evolution.name,
            color = colorScheme.onSurface,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 28.sp,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Icon(
                imageVector = Icons.Default.FitnessCenter,
                contentDescription = null,
                tint = colorScheme.onSurfaceVariant,
                modifier = Modifier.size(12.dp),
            )
            Text(
                text = stringResource(Res.string.evolution_screen_session_count, evolution.sessionCount),
                color = colorScheme.onSurfaceVariant,
                fontSize = 14.sp,
                lineHeight = 20.sp,
            )
            Box(
                modifier = Modifier
                    .size(3.dp)
                    .clip(CircleShape)
                    .background(colorScheme.outline.copy(alpha = 0.4f)),
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                contentDescription = null,
                tint = colorScheme.onSurfaceVariant,
                modifier = Modifier.size(12.dp),
            )
            Text(
                text = stringResource(Res.string.evolution_screen_weight_delta, deltaText),
                color = deltaColor,
                fontSize = 14.sp,
                lineHeight = 20.sp,
            )
        }
    }
}

@Composable
private fun EvolutionDetailDialog(evolution: ExerciseEvolution, onDismiss: () -> Unit) {
    val colorScheme = MaterialTheme.colorScheme

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(colorScheme.surface)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Text(
                text = evolution.name,
                color = colorScheme.onSurface,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = evolution.routineNames.joinToString(", "),
                color = colorScheme.onSurfaceVariant,
                fontSize = 14.sp,
            )
            EvolutionChart(history = evolution.history)
            Row(
                modifier = Modifier.fillMaxWidth(),
                // Centradas con hueco fijo: con SpaceBetween las dos métricas se iban a los extremos.
                horizontalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterHorizontally),
            ) {
                DetailMetric(
                    label = stringResource(Res.string.evolution_detail_weight_label),
                    value = stringResource(
                        Res.string.evolution_detail_weight_value,
                        evolution.firstWeight.toString(),
                        evolution.lastWeight.toString(),
                    ),
                )
                DetailMetric(
                    label = stringResource(Res.string.evolution_detail_reps_label),
                    value = stringResource(Res.string.evolution_detail_reps_value, evolution.firstReps, evolution.lastReps),
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(colorScheme.surfaceVariant)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) { onDismiss() }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(Res.string.evolution_detail_close),
                    color = colorScheme.onSurfaceVariant,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}

@Composable
private fun DetailMetric(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = value,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 11.sp,
            letterSpacing = 0.5.sp,
        )
    }
}

@Composable
private fun SeedTestDataButton(onClick: () -> Unit) {
    val colorScheme = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, colorScheme.outline.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) { onClick() }
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(Res.string.evolution_screen_seed_test_data),
            color = colorScheme.onSurfaceVariant,
            fontSize = 13.sp,
        )
    }
}
