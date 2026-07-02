package dev.bonygod.gymroutine.core.utils

import androidx.compose.runtime.Composable
import gymroutine.composeapp.generated.resources.Res
import gymroutine.composeapp.generated.resources.day_item_friday
import gymroutine.composeapp.generated.resources.day_item_monday
import gymroutine.composeapp.generated.resources.day_item_saturday
import gymroutine.composeapp.generated.resources.day_item_sunday
import gymroutine.composeapp.generated.resources.day_item_thursday
import gymroutine.composeapp.generated.resources.day_item_tuesday
import gymroutine.composeapp.generated.resources.day_item_wednesday
import gymroutine.composeapp.generated.resources.format_days_no_days
import org.jetbrains.compose.resources.stringResource

/**
 * Normalizes any known day token (abbreviated or full name, with or without
 * accents, any case) to a canonical uppercase abbreviation like "LUN", "MIÉ",
 * etc.  Returns null for unrecognised tokens.
 */
fun normalizeDayToken(token: String): String? = when (token.trim().uppercase()) {
    // Canonical abbreviations (already correct)
    "LUN" -> "LUN"
    "MAR" -> "MAR"
    "MIÉ", "MIE" -> "MIÉ"
    "JUE" -> "JUE"
    "VIE" -> "VIE"
    "SÁB", "SAB" -> "SÁB"
    "DOM" -> "DOM"
    // Full Spanish day names
    "LUNES" -> "LUN"
    "MARTES" -> "MAR"
    "MIÉRCOLES", "MIERCOLES" -> "MIÉ"
    "JUEVES" -> "JUE"
    "VIERNES" -> "VIE"
    "SÁBADO", "SABADO" -> "SÁB"
    "DOMINGO" -> "DOM"
    // English abbreviations (values/strings.xml default locale)
    "MON", "MONDAY" -> "LUN"
    "TUE", "TUESDAY" -> "MAR"
    "WED", "WEDNESDAY" -> "MIÉ"
    "THU", "THURSDAY" -> "JUE"
    "FRI", "FRIDAY" -> "VIE"
    "SAT", "SATURDAY" -> "SÁB"
    "SUN", "SUNDAY" -> "DOM"
    // Catalan abbreviations (values-ca/strings.xml)
    "DL", "DILLUNS" -> "LUN"
    "DT", "DIMARTS" -> "MAR"
    "DC", "DIMECRES" -> "MIÉ"
    "DJ", "DIJOUS" -> "JUE"
    "DV", "DIVENDRES" -> "VIE"
    "DS", "DISSABTE" -> "SÁB"
    "DG", "DIUMENGE" -> "DOM"
    else -> null
}

fun dayAbbrToFullName(abbr: String): String = when (abbr.uppercase()) {
    "LUN" -> "Lunes"
    "MAR" -> "Martes"
    "MIÉ" -> "Miércoles"
    "JUE" -> "Jueves"
    "VIE" -> "Viernes"
    "SÁB" -> "Sábado"
    "DOM" -> "Domingo"
    else -> abbr
}

fun monthName(monthNumber: Int): String = when (monthNumber) {
    1 -> "enero"
    2 -> "febrero"
    3 -> "marzo"
    4 -> "abril"
    5 -> "mayo"
    6 -> "junio"
    7 -> "julio"
    8 -> "agosto"
    9 -> "septiembre"
    10 -> "octubre"
    11 -> "noviembre"
    12 -> "diciembre"
    else -> ""
}

/** Formats a comma-separated canonical day string (e.g. "LUN,MIÉ") into a
 *  human-readable label (e.g. "Lun, Mié"). Returns "Sin días asignados" when blank. */
fun formatDays(days: String): String {
    if (days.isBlank()) return "Sin días asignados"
    val selected = days.split(",").map { it.trim().uppercase() }.toSet()
    return Day.entries.filter { it.abbr in selected }.joinToString(", ") { it.display }
}

/** Localized version of [formatDays] for use inside Composable functions. */
@Composable
fun formatDaysLocalized(days: String): String {
    // All stringResource calls must be at the top level of the @Composable body,
    // not inside lambdas (map/joinToString are not @Composable contexts).
    val noDays = stringResource(Res.string.format_days_no_days)
    val labels = mapOf(
        Day.MONDAY to stringResource(Res.string.day_item_monday),
        Day.TUESDAY to stringResource(Res.string.day_item_tuesday),
        Day.WEDNESDAY to stringResource(Res.string.day_item_wednesday),
        Day.THURSDAY to stringResource(Res.string.day_item_thursday),
        Day.FRIDAY to stringResource(Res.string.day_item_friday),
        Day.SATURDAY to stringResource(Res.string.day_item_saturday),
        Day.SUNDAY to stringResource(Res.string.day_item_sunday),
    )
    if (days.isBlank()) return noDays
    val selected = days.split(",").map { it.trim().uppercase() }.toSet()
    return Day.entries
        .filter { it.abbr in selected }
        .joinToString(", ") { labels[it] ?: it.display }
}

