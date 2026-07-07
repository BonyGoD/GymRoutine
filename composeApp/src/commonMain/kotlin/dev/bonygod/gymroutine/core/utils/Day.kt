package dev.bonygod.gymroutine.core.utils

import gymroutine.composeapp.generated.resources.Res
import gymroutine.composeapp.generated.resources.day_item_friday
import gymroutine.composeapp.generated.resources.day_item_monday
import gymroutine.composeapp.generated.resources.day_item_saturday
import gymroutine.composeapp.generated.resources.day_item_sunday
import gymroutine.composeapp.generated.resources.day_item_thursday
import gymroutine.composeapp.generated.resources.day_item_tuesday
import gymroutine.composeapp.generated.resources.day_item_wednesday
import org.jetbrains.compose.resources.StringResource

/**
 * Canonical representation of a weekday.
 *
 * - [abbr]    — canonical uppercase abbreviation stored in Firestore ("LUN", "MIÉ", …)
 * - [display] — short label shown in the UI in Spanish fallback ("Lun", "Mié", …)
 * - [res]     — localized string resource for UI display (use [stringResource]/[getString])
 *
 * [entries] preserves declaration order (Mon → Sun), so it replaces any
 * hand-written DAY_ORDER list.
 */
enum class Day(val abbr: String, val display: String, val res: StringResource) {
    MONDAY("LUN", "Lun", Res.string.day_item_monday),
    TUESDAY("MAR", "Mar", Res.string.day_item_tuesday),
    WEDNESDAY("MIÉ", "Mié", Res.string.day_item_wednesday),
    THURSDAY("JUE", "Jue", Res.string.day_item_thursday),
    FRIDAY("VIE", "Vie", Res.string.day_item_friday),
    SATURDAY("SÁB", "Sáb", Res.string.day_item_saturday),
    SUNDAY("DOM", "Dom", Res.string.day_item_sunday),
    ;

    companion object {
        /** Returns the [Day] whose [abbr] matches, or null for unknown tokens. */
        fun fromAbbr(abbr: String): Day? = entries.firstOrNull { it.abbr == abbr }
    }
}
