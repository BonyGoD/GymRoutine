package dev.bonygod.gymroutine.home.domain.model

import dev.bonygod.gymroutine.routines.domain.model.Routine

/**
 * Un entreno de [routine] planificado para [plannedDate] (ISO-8601 "YYYY-MM-DD") que no se hizo
 * y todavía cabe recuperar dentro de la semana natural en curso (lunes a domingo).
 *
 * [dayAbbr] es la abreviatura española del día ("LUN", "MAR"...) para no tener que volver a
 * derivarla en la UI.
 */
data class PendingWorkout(
    val routine: Routine,
    val plannedDate: String,
    val dayAbbr: String,
)
