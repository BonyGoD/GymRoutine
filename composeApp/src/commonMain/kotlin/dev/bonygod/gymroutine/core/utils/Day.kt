package dev.bonygod.gymroutine.core.utils

/**
 * Canonical representation of a weekday.
 *
 * - [abbr]    — canonical uppercase abbreviation stored in Firestore ("LUN", "MIÉ", …)
 * - [display] — short label shown in the UI ("Lun", "Mié", …)
 *
 * [entries] preserves declaration order (Mon → Sun), so it replaces any
 * hand-written DAY_ORDER list.
 */
enum class Day(val abbr: String, val display: String) {
    MONDAY("LUN", "Lun"),
    TUESDAY("MAR", "Mar"),
    WEDNESDAY("MIÉ", "Mié"),
    THURSDAY("JUE", "Jue"),
    FRIDAY("VIE", "Vie"),
    SATURDAY("SÁB", "Sáb"),
    SUNDAY("DOM", "Dom"),
    ;

    companion object {
        /** Returns the [Day] whose [abbr] matches, or null for unknown tokens. */
        fun fromAbbr(abbr: String): Day? = entries.firstOrNull { it.abbr == abbr }
    }
}
