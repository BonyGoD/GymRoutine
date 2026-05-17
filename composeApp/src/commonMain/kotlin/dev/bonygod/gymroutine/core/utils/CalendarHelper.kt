package dev.bonygod.gymroutine.core.utils

import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import kotlinx.datetime.minus

data class DayItem(
    val abbr: String,
    val num: Int,
    val date: LocalDate,
    val isToday: Boolean,
)

fun buildCalendarDays(today: LocalDate): List<DayItem> {
    val start = today.minus(DatePeriod(months = 1))
    val end = today.plus(DatePeriod(months = 1))
    val days = mutableListOf<DayItem>()
    var cursor = start
    while (cursor <= end) {
        val abbr =
            when (cursor.dayOfWeek) {
                DayOfWeek.MONDAY -> "LUN"
                DayOfWeek.TUESDAY -> "MAR"
                DayOfWeek.WEDNESDAY -> "MIÉ"
                DayOfWeek.THURSDAY -> "JUE"
                DayOfWeek.FRIDAY -> "VIE"
                DayOfWeek.SATURDAY -> "SÁB"
                DayOfWeek.SUNDAY -> "DOM"
            }
        days += DayItem(abbr = abbr, num = cursor.day, date = cursor, isToday = cursor == today)
        cursor = cursor.plus(DatePeriod(days = 1))
    }
    return days
}
