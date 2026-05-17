package dev.bonygod.gymroutine.home.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bonygod.gymroutine.core.utils.DayItem
import dev.bonygod.gymroutine.core.utils.buildCalendarDays
import dev.bonygod.gymroutine.home.ui.HomeViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock

private val GoldIcon = Color(0xFFE7C26C)
private val BlueIcon = Color(0xFF00A5D7)

@Composable
fun HomeScreen(viewModel: HomeViewModel = koinViewModel()) {
    val userName by viewModel.userName.collectAsState()
    val today = remember { Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date }
    val days = remember(today) { buildCalendarDays(today) }
    val todayIndex = remember(days) { days.indexOfFirst { it.isToday }.coerceAtLeast(0) }
    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(colorScheme.background)
                .verticalScroll(rememberScrollState()),
    ) {
        // ── Top Header ────────────────────────────────────────────────────────
        Row(
            modifier =
                Modifier
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
                modifier =
                    Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(colorScheme.surfaceVariant)
                        .border(1.dp, colorScheme.outline.copy(alpha = 0.15f), CircleShape),
            )
        }

        // ── Main content ──────────────────────────────────────────────────────
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, bottom = 128.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp),
        ) {
            GreetingSection(userName = userName)
            CalendarSection(days = days, todayIndex = todayIndex)
            WorkoutCTASection()
            QuickStatsBento()
        }
    }
}

// ── Section 1: Greeting ───────────────────────────────────────────────────────
@Composable
private fun GreetingSection(userName: String) {
    val displayName = if (userName.isNotBlank()) userName else "…"
    Text(
        text = "Buenos días, $displayName",
        color = MaterialTheme.colorScheme.onBackground,
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = (-0.56).sp,
        modifier = Modifier.fillMaxWidth(),
    )
}

// ── Section 2: Real Horizontal Calendar ──────────────────────────────────────
@Composable
private fun CalendarSection(
    days: List<DayItem>,
    todayIndex: Int,
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
        modifier =
            Modifier
                .fillMaxWidth()
                .height(80.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(days.size) { i ->
            DayCell(day = days[i])
        }
    }
}

@Composable
private fun DayCell(day: DayItem) {
    val colorScheme = MaterialTheme.colorScheme
    val bgColor = if (day.isToday) colorScheme.primary else colorScheme.surface
    val textColor = if (day.isToday) colorScheme.onPrimary else colorScheme.onSurfaceVariant
    val numColor = if (day.isToday) colorScheme.onPrimary else colorScheme.onSurface
    val numWeight = if (day.isToday) FontWeight.Bold else FontWeight.Normal
    val borderColor = if (day.isToday) colorScheme.primary else colorScheme.outline.copy(alpha = 0.15f)

    Box(
        modifier =
            Modifier
                .size(width = 60.dp, height = 72.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(bgColor)
                .border(1.dp, borderColor, RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
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
        }
    }
}

// ── Section 3: Workout CTA ────────────────────────────────────────────────────
@Composable
private fun WorkoutCTASection() {
    val colorScheme = MaterialTheme.colorScheme
    Box(
        modifier =
            Modifier
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
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = "Día de Pierna",
                    color = colorScheme.onSurface,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.56).sp,
                    modifier = Modifier.fillMaxWidth(),
                )
                Text(
                    text = "Enfoque Hipertrofia",
                    color = colorScheme.primary,
                    fontSize = 16.sp,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(Icons.Default.AccessTime, null, tint = colorScheme.onSurfaceVariant, modifier = Modifier.size(16.dp))
                    Text("60 min", color = colorScheme.onSurfaceVariant, fontSize = 14.sp)
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(Icons.Default.FitnessCenter, null, tint = colorScheme.onSurfaceVariant, modifier = Modifier.size(16.dp))
                    Text("6 Ejercicios", color = colorScheme.onSurfaceVariant, fontSize = 14.sp)
                }
            }

            Button(
                onClick = {},
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .height(64.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary),
            ) {
                Icon(Icons.Default.PlayArrow, null, tint = colorScheme.onPrimary, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text("INICIAR ENTRENAMIENTO", color = colorScheme.onPrimary, fontSize = 16.sp)
            }
        }

        // "LISTO" badge – top-right overlay
        Box(
            modifier =
                Modifier
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
                Box(
                    Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(colorScheme.primary),
                )
                Text(
                    "LISTO",
                    color = colorScheme.onSurface,
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
private fun QuickStatsBento() {
    val colorScheme = MaterialTheme.colorScheme
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        StatCard(
            modifier = Modifier.fillMaxWidth(),
            icon = { Icon(Icons.Default.Star, null, tint = GoldIcon, modifier = Modifier.size(15.dp)) },
            label = "RÉCORDS ESTA SEMANA",
            bigNumber = "3",
            bigUnit = null,
            subtitle = "Squat, Deadlift, OHP",
        )

        IntrinsicHeightRow {
            StatCard(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                icon = { Icon(Icons.Default.BarChart, null, tint = BlueIcon, modifier = Modifier.size(15.dp)) },
                label = "VOLUMEN\nTOTAL",
                bigNumber = "24",
                bigUnit = "k",
                subtitle = "kg levantados",
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
                bigNumber = "92",
                bigUnit = "%",
                subtitle = "Últimos 30 días",
            )
        }
    }
}

@Composable
private fun IntrinsicHeightRow(content: @Composable RowScope.() -> Unit) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max),
        content = content,
    )
}

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
        modifier =
            modifier
                .clip(RoundedCornerShape(32.dp))
                .background(colorScheme.surface)
                .border(1.dp, colorScheme.outline.copy(alpha = 0.15f), RoundedCornerShape(32.dp))
                .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            modifier =
                Modifier
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
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
