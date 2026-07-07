package dev.bonygod.gymroutine.core.utils

import gymroutine.composeapp.generated.resources.Res
import gymroutine.composeapp.generated.resources.day_item_friday
import gymroutine.composeapp.generated.resources.day_item_monday
import gymroutine.composeapp.generated.resources.day_item_saturday
import gymroutine.composeapp.generated.resources.day_item_sunday
import gymroutine.composeapp.generated.resources.day_item_thursday
import gymroutine.composeapp.generated.resources.day_item_tuesday
import gymroutine.composeapp.generated.resources.day_item_wednesday
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import org.jetbrains.compose.resources.getString

data class DayItem(
    val abbr: String,
    val num: Int,
    val date: LocalDate,
    val isToday: Boolean,
)

suspend fun buildCalendarDays(today: LocalDate): List<DayItem> {
    val start = today.minus(DatePeriod(months = 1))
    val end = today.plus(DatePeriod(months = 1))
    val days = mutableListOf<DayItem>()
    var cursor = start
    while (cursor <= end) {
        val abbr =
            when (cursor.dayOfWeek) {
                DayOfWeek.MONDAY -> getString(Res.string.day_item_monday)
                DayOfWeek.TUESDAY -> getString(Res.string.day_item_tuesday)
                DayOfWeek.WEDNESDAY -> getString(Res.string.day_item_wednesday)
                DayOfWeek.THURSDAY -> getString(Res.string.day_item_thursday)
                DayOfWeek.FRIDAY -> getString(Res.string.day_item_friday)
                DayOfWeek.SATURDAY -> getString(Res.string.day_item_saturday)
                DayOfWeek.SUNDAY -> getString(Res.string.day_item_sunday)
            }
        days += DayItem(abbr = abbr, num = cursor.day, date = cursor, isToday = cursor == today)
        cursor = cursor.plus(DatePeriod(days = 1))
    }
    return days
}
