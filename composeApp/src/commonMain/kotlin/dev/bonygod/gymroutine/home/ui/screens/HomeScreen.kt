package dev.bonygod.gymroutine.home.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bonygod.gymroutine.core.theme.GoldIcon
import dev.bonygod.gymroutine.core.theme.OrangeIcon
import dev.bonygod.gymroutine.core.utils.DayItem
import dev.bonygod.gymroutine.core.utils.buildCalendarDays
import dev.bonygod.gymroutine.core.utils.dayAbbrToFullName
import dev.bonygod.gymroutine.core.utils.monthName
import dev.bonygod.gymroutine.home.ui.HomeViewModel
import dev.bonygod.gymroutine.home.ui.interactions.HomeEvent
import dev.bonygod.gymroutine.routines.domain.mapper.hasRoutineForDay
import dev.bonygod.gymroutine.routines.domain.mapper.routinesForDay
import dev.bonygod.gymroutine.routines.domain.model.Exercise
import dev.bonygod.gymroutine.routines.domain.model.Routine
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(vmKey: String = "", viewModel: HomeViewModel = koinViewModel(key = vmKey.ifBlank { null })) {
    val state by viewModel.state.collectAsState()
    val today = remember { Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date }
    val days = remember(today) { buildCalendarDays(today) }
    val todayIndex = remember(days) { days.indexOfFirst { it.isToday }.coerceAtLeast(0) }
    val todayAbbr = remember(days, todayIndex) { days.getOrNull(todayIndex)?.abbr.orEmpty() }
    val todayRoutines = remember(state.routines, todayAbbr) { state.routines.routinesForDay(todayAbbr) }
    val colorScheme = MaterialTheme.colorScheme

    var selectedDay by remember { mutableStateOf<DayItem?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background),
    ) {
        // ── FIJO: Top Header ──────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, start = 24.dp, end = 24.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "GYMROUTINE",
                color = colorScheme.primary,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = (-1).sp,
            )
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(colorScheme.surfaceVariant)
                    .border(1.dp, colorScheme.outline.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                if (state.userName.isNotBlank()) {
                    Text(
                        text = state.userName.first().uppercase(),
                        color = colorScheme.primary,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                    )
                } else {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = colorScheme.primary,
                        modifier = Modifier.size(40.dp),
                    )
                }
            }
        }

        // ── FIJO: Saludo ──────────────────────────────────────────────────────
        GreetingSection(
            userName = state.userName,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 4.dp),
        )

        // ── FIJO: Calendario ─────────────────────────────────────────────────
        CalendarSection(
            days = days,
            todayIndex = todayIndex,
            routines = state.routines,
            onDayClick = { day -> selectedDay = day },
        )

        // ── SCROLL: Contenido principal ───────────────────────────────────────
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 128.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp),
        ) {
            WorkoutCTASection(
                todayRoutines = todayRoutines,
                isTodayCompleted = state.isTodayCompleted,
                onStart = {
                    val routine = todayRoutines.firstOrNull()
                    viewModel.onEvent(
                        HomeEvent.OnStartWorkout(
                            routineId = routine?.id.orEmpty(),
                            routineName = routine?.name.orEmpty(),
                        ),
                    )
                },
            )
            QuickStatsBento(
                todayKcal = state.todayKcal,
                consistency = state.consistency,
                weekRecordCount = state.weekRecordCount,
                weekRecordSubtitle = state.weekRecordSubtitle,
            )
        }
    }

    // ── Bottom Sheet ──────────────────────────────────────────────────────────
    selectedDay?.let { day ->
        ModalBottomSheet(
            onDismissRequest = { selectedDay = null },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface,
        ) {
            RoutineDayBottomSheet(day = day, routines = state.routines)
        }
    }
}

// ── Section 1: Greeting ───────────────────────────────────────────────────────
@Composable
private fun GreetingSection(userName: String, modifier: Modifier = Modifier) {
    val displayName = if (userName.isNotBlank()) userName else "…"
    Text(
        text = "Buenos días, $displayName",
        color = MaterialTheme.colorScheme.onBackground,
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = (-0.56).sp,
        modifier = modifier,
    )
}

// ── Section 2: Calendario horizontal ─────────────────────────────────────────
@Composable
private fun CalendarSection(
    days: List<DayItem>,
    todayIndex: Int,
    routines: List<Routine>,
    onDayClick: (DayItem) -> Unit,
) {
    val listState = rememberLazyListState()
    val coroutine = rememberCoroutineScope()

    LaunchedEffect(todayIndex) {
        coroutine.launch {
            listState.scrollToItem(index = (todayIndex - 2).coerceAtLeast(0))
        }
    }

    LazyRow(
        state = listState,
        modifier = Modifier
            .fillMaxWidth()
            .height(88.dp),
        contentPadding = PaddingValues(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(days.size) { i ->
            val day = days[i]
            val hasRoutine = routines.hasRoutineForDay(day.abbr)
            DayCell(
                day = day,
                hasRoutine = hasRoutine,
                onClick = { onDayClick(day) },
            )
        }
    }
}

@Composable
private fun DayCell(
    day: DayItem,
    hasRoutine: Boolean,
    onClick: () -> Unit,
) {
    val colorScheme = MaterialTheme.colorScheme
    val bgColor = if (day.isToday) colorScheme.primary else colorScheme.surface
    val textColor = if (day.isToday) colorScheme.onPrimary else colorScheme.onSurfaceVariant
    val numColor = if (day.isToday) colorScheme.onPrimary else colorScheme.onSurface
    val numWeight = if (day.isToday) FontWeight.Bold else FontWeight.Normal
    val borderColor = when {
        day.isToday -> colorScheme.primary
        hasRoutine -> colorScheme.primary.copy(alpha = 0.5f)
        else -> colorScheme.outline.copy(alpha = 0.15f)
    }
    val dotColor = if (day.isToday) colorScheme.onPrimary.copy(alpha = 0.85f) else colorScheme.primary

    Box(
        modifier = Modifier
            .size(width = 60.dp, height = 80.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .then(if (hasRoutine) Modifier.clickable(onClick = onClick) else Modifier),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = day.abbr,
                color = textColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
            )
            Text(
                text = day.num.toString(),
                color = numColor,
                fontSize = 14.sp,
                fontWeight = numWeight,
                textAlign = TextAlign.Center,
            )
            // Indicador de rutina (dot) — siempre ocupa el mismo espacio
            if (hasRoutine) {
                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .clip(CircleShape)
                        .background(dotColor),
                )
            } else {
                Spacer(Modifier.height(5.dp))
            }
        }
    }
}

// ── Bottom Sheet: resumen de rutina ──────────────────────────────────────────

@Composable
private fun RoutineDayBottomSheet(
    day: DayItem,
    routines: List<Routine>,
) {
    val colorScheme = MaterialTheme.colorScheme
    val routinesForDay = remember(day.abbr, routines) {
        routines.routinesForDay(day.abbr)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, top = 8.dp, bottom = 48.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        // Cabecera: día y fecha
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = dayAbbrToFullName(day.abbr),
                color = colorScheme.onSurface,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.52).sp,
            )
            Text(
                text = "${day.num} de ${monthName(day.date.monthNumber)}",
                color = colorScheme.onSurfaceVariant,
                fontSize = 14.sp,
            )
        }

        // Lista de rutinas con sus ejercicios
        routinesForDay.forEach { routine ->
            RoutineSummaryItem(routine = routine)
        }
    }
}

@Composable
private fun RoutineSummaryItem(routine: Routine) {
    val colorScheme = MaterialTheme.colorScheme
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(colorScheme.surfaceVariant)
            .border(1.dp, colorScheme.outline.copy(alpha = 0.12f), RoundedCornerShape(20.dp))
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = routine.name.uppercase(),
            color = colorScheme.primary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.2.sp,
        )
        routine.exercises.forEach { exercise ->
            ExerciseRow(exercise = exercise)
        }
    }
}

@Composable
private fun ExerciseRow(exercise: Exercise) {
    val colorScheme = MaterialTheme.colorScheme
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(colorScheme.primary.copy(alpha = 0.6f)),
            )
            Text(
                text = exercise.name,
                color = colorScheme.onSurface,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
            )
        }
        Text(
            text = "${exercise.sets}×${exercise.reps}",
            color = colorScheme.onSurfaceVariant,
            fontSize = 13.sp,
        )
    }
}

// ── Section 3: Workout CTA ────────────────────────────────────────────────────
@Composable
private fun WorkoutCTASection(todayRoutines: List<Routine>, isTodayCompleted: Boolean, onStart: () -> Unit) {
    val colorScheme = MaterialTheme.colorScheme
    val routine = todayRoutines.firstOrNull()
    val isRestDay = routine == null
    val routineName = routine?.name ?: "Día de descanso"
    val exerciseCount = routine?.exercises?.size ?: 0
    val estimatedMinutes = routine?.exercises?.sumOf { ex ->
        val rest = ex.restSeconds.coerceAtLeast(60)
        ex.sets * (ex.reps * 2 + rest)
    }?.div(60) ?: 0

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(colorScheme.surface)
            .border(1.dp, colorScheme.outline.copy(alpha = 0.15f), RoundedCornerShape(32.dp))
            .padding(32.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = routineName,
                color = colorScheme.onSurface,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.56).sp,
                modifier = Modifier.fillMaxWidth(),
            )

            if (!isRestDay) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (estimatedMinutes > 0) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(Icons.Default.AccessTime, null, tint = colorScheme.onSurfaceVariant, modifier = Modifier.size(16.dp))
                            Text("$estimatedMinutes min", color = colorScheme.onSurfaceVariant, fontSize = 14.sp)
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(Icons.Default.FitnessCenter, null, tint = colorScheme.onSurfaceVariant, modifier = Modifier.size(16.dp))
                        Text("$exerciseCount Ejercicios", color = colorScheme.onSurfaceVariant, fontSize = 14.sp)
                    }
                }
            }

            Button(
                onClick = onStart,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .height(64.dp),
                shape = CircleShape,
                enabled = !isTodayCompleted,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorScheme.primary,
                    disabledContainerColor = Color(0xFF1B5E20),
                ),
            ) {
                Icon(
                    if (isTodayCompleted) Icons.Default.CheckCircle else Icons.Default.PlayArrow,
                    null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp),
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    if (isTodayCompleted) "ENTRENAMIENTO COMPLETADO" else "INICIAR ENTRENAMIENTO",
                    color = Color.White,
                    fontSize = 15.sp,
                )
            }
        }

        // Badge: LISTO / TERMINADO – top-right overlay
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .clip(CircleShape)
                .background(colorScheme.surfaceVariant)
                .border(1.dp, colorScheme.outline.copy(alpha = 0.15f), CircleShape)
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (isTodayCompleted) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(10.dp),
                    )
                } else {
                    Box(
                        Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(colorScheme.primary),
                    )
                }
                Text(
                    if (isTodayCompleted) "TERMINADO" else "LISTO",
                    color = if (isTodayCompleted) Color(0xFF4CAF50) else colorScheme.onSurface,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = (0.96).sp,
                )
            }
        }
    }
}

// ── Section 4: Quick Stats Bento ─────────────────────────────────────────────
@Composable
private fun QuickStatsBento(
    todayKcal: Int,
    consistency: Int,
    weekRecordCount: Int,
    weekRecordSubtitle: String,
) {
    val colorScheme = MaterialTheme.colorScheme
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        StatCard(
            modifier = Modifier.fillMaxWidth(),
            icon = { Icon(Icons.Default.Star, null, tint = GoldIcon, modifier = Modifier.size(15.dp)) },
            label = "RÉCORDS ESTA SEMANA",
            bigNumber = if (weekRecordCount > 0) weekRecordCount.toString() else "--",
            bigUnit = null,
            subtitle = weekRecordSubtitle.ifBlank { "Sin entrenos esta semana" },
        )

        IntrinsicHeightRow {
            StatCard(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                icon = { Icon(Icons.Default.Whatshot, null, tint = OrangeIcon, modifier = Modifier.size(15.dp)) },
                label = "KCAL\nQUEMADAS",
                bigNumber = if (todayKcal > 0) todayKcal.toString() else "--",
                bigUnit = if (todayKcal > 0) "kcal" else null,
                subtitle = if (todayKcal > 0) "Estimadas hoy" else "Sin entreno hoy",
            )
            Spacer(Modifier.width(12.dp))
            StatCard(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                icon = {
                    Icon(
                        Icons.AutoMirrored.Filled.TrendingUp,
                        null,
                        tint = colorScheme.primary,
                        modifier = Modifier.size(15.dp),
                    )
                },
                label = "CONSTANCIA",
                bigNumber = if (consistency > 0) consistency.toString() else "--",
                bigUnit = if (consistency > 0) "%" else null,
                subtitle = "Esta semana",
            )
        }
    }
}

@Composable
private fun IntrinsicHeightRow(content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max),
        content = content,
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    label: String,
    bigNumber: String,
    bigUnit: String?,
    subtitle: String,
) {
    val colorScheme = MaterialTheme.colorScheme
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(32.dp))
            .background(colorScheme.surface)
            .border(1.dp, colorScheme.outline.copy(alpha = 0.15f), RoundedCornerShape(32.dp))
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            icon()
            Text(
                text = label,
                color = colorScheme.onSurfaceVariant,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = (0.96).sp,
                lineHeight = 16.sp,
            )
        }
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            itemVerticalAlignment = Alignment.Bottom,
        ) {
            Text(
                text = bigNumber,
                color = colorScheme.onSurface,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-1.92).sp,
                lineHeight = 52.8.sp,
            )
            if (bigUnit != null) {
                Text(
                    text = bigUnit,
                    color = colorScheme.onSurfaceVariant,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp),
                )
            }
        }
        Text(
            text = subtitle,
            color = colorScheme.onSurfaceVariant,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}
