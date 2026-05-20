package dev.bonygod.gymroutine.core.utils

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
