package dev.bonygod.gymroutine.history.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bonygod.gymroutine.core.utils.monthName
import dev.bonygod.gymroutine.history.ui.HistoryViewModel
import dev.bonygod.gymroutine.workout.domain.model.WorkoutLog
import gymroutine.composeapp.generated.resources.Res
import gymroutine.composeapp.generated.resources.history_screen_date_day_month
import gymroutine.composeapp.generated.resources.history_screen_date_today
import gymroutine.composeapp.generated.resources.history_screen_date_yesterday
import gymroutine.composeapp.generated.resources.history_screen_empty
import gymroutine.composeapp.generated.resources.history_screen_title
import gymroutine.composeapp.generated.resources.history_screen_workout_fallback
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock

@Composable
fun HistoryScreen(vmKey: String = "", viewModel: HistoryViewModel = koinViewModel(key = vmKey.ifBlank { null })) {
    val state by viewModel.state.collectAsState()
    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background),
    ) {
        Text(
            text = stringResource(Res.string.history_screen_title),
            color = colorScheme.primary,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-0.56).sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 96.dp, start = 24.dp, end = 24.dp, bottom = 16.dp),
        )

        when {
            state.isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = colorScheme.primary)
                }
            }

            state.logs.isEmpty() -> {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 48.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Icon(
                            Icons.Default.FitnessCenter,
                            contentDescription = null,
                            tint = colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(48.dp),
                        )
                        Text(
                            stringResource(Res.string.history_screen_empty),
                            color = colorScheme.onSurfaceVariant,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(state.logs) { log ->
                        WorkoutLogCard(log = log)
                    }
                    item { Spacer(Modifier.height(88.dp)) }
                }
            }
        }
    }
}

@Composable
private fun WorkoutLogCard(log: WorkoutLog) {
    val colorScheme = MaterialTheme.colorScheme

    val todayText = stringResource(Res.string.history_screen_date_today)
    val yesterdayText = stringResource(Res.string.history_screen_date_yesterday)
    val dayMonthFormat = stringResource(Res.string.history_screen_date_day_month)
    val workoutFallback = stringResource(Res.string.history_screen_workout_fallback)

    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val yesterday = today.minus(1, DateTimeUnit.DAY)
    val dateLabel = try {
        val d = LocalDate.parse(log.date)
        when (d) {
            today -> todayText
            yesterday -> yesterdayText
            else -> dayMonthFormat.format(d.dayOfMonth, monthName(d.monthNumber))
        }
    } catch (e: Exception) {
        log.date
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(colorScheme.surface)
            .border(1.dp, colorScheme.outline.copy(alpha = 0.15f), RoundedCornerShape(20.dp))
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(colorScheme.primary.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                Icons.Default.FitnessCenter,
                contentDescription = null,
                tint = colorScheme.primary,
                modifier = Modifier.size(22.dp),
            )
        }

        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = log.routineName.ifBlank { workoutFallback },
                color = colorScheme.onSurface,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = dateLabel,
                color = colorScheme.onSurfaceVariant,
                fontSize = 13.sp,
            )
        }
    }
}
