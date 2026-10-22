package dev.bonygod.gymroutine.wear.model

import java.time.DayOfWeek

fun DayOfWeek.toDayToken(): String = when (this) {
    DayOfWeek.MONDAY -> "LUN"
    DayOfWeek.TUESDAY -> "MAR"
    DayOfWeek.WEDNESDAY -> "MIÉ"
    DayOfWeek.THURSDAY -> "JUE"
    DayOfWeek.FRIDAY -> "VIE"
    DayOfWeek.SATURDAY -> "SÁB"
    DayOfWeek.SUNDAY -> "DOM"
}
