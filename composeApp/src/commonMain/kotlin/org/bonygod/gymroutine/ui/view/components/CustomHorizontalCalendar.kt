package org.bonygod.gymroutine.ui.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import org.bonygod.gymroutine.ui.theme.CustomBlack
import org.bonygod.gymroutine.ui.theme.CustomYellow

@Composable
fun CustomHorizontalCalendar(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val dates = remember { generateDates() }
    val todayIndex =
        dates.indexOf(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date)
    val listState = rememberLazyListState()
    val locale = Locale.current


    LaunchedEffect(todayIndex) {
        listState.scrollToItem(todayIndex)
    }

    LazyRow(
        state = listState,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(dates) { date ->
            val dayName = DayOfWeekHelper.getShortDayName(date.dayOfWeek, locale.language)
            val dayNumber = date.dayOfMonth.toString()
            Surface(
                color = Color.Transparent,
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .clickable { onDateSelected(date) }
            ) {
                Column(
                    horizontalAlignment = CenterHorizontally,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = dayName,
                        color = if (date == selectedDate) CustomYellow else CustomBlack,
                        fontSize = 16.sp
                    )
                    Text(
                        text = dayNumber,
                        color = if (date == selectedDate) CustomYellow else CustomBlack,
                        fontSize = 24.sp
                    )
                }
            }
        }
    }
}

object DayOfWeekHelper {
    private val translations = mapOf(
        "en" to listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"),
        "es" to listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom"),
        "ca" to listOf("Dil", "Dim", "Dim", "Dij", "Div", "Dis", "Diu")
    )

    fun getShortDayName(dayOfWeek: DayOfWeek, language: String = "en"): String {
        val lang = translations[language] ?: translations["en"]!!
        return lang[dayOfWeek.ordinal] // `ordinal` devuelve 0 para Lunes, 1 para Martes, etc.
    }
}



fun generateDates(): List<LocalDate> {
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.minus(
        30,
        DateTimeUnit.DAY
    )
    return (0..61).map { today.plus(it, DateTimeUnit.DAY) }
}