package dev.bonygod.gymroutine.home.domain

import dev.bonygod.gymroutine.core.utils.toSpanishAbbr
import dev.bonygod.gymroutine.home.domain.model.PendingWorkout
import dev.bonygod.gymroutine.routines.domain.mapper.routinesForDay
import dev.bonygod.gymroutine.routines.domain.model.Routine
import dev.bonygod.gymroutine.workout.domain.model.WorkoutLog
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus

/** Clave estable de un pendiente, usada tanto para derivarlo como para descartarlo. */
fun pendingWorkoutKey(plannedDate: String, routineId: String): String = "$plannedDate|$routineId"

/**
 * Deriva los entrenos planificados y no hechos de la semana natural en curso (lunes a domingo),
 * recorriendo del lunes hasta **ayer** — hoy nunca cuenta como pendiente, tiene su propio CTA.
 *
 * Un entreno cuenta como hecho si hay un log completado en la fecha planificada, **o** un log
 * completado marcado como recuperación de esa fecha ([WorkoutLog.recoveredFrom]). Sin esta segunda
 * condición, recuperar un entreno saltado en un día distinto del planificado haría que el
 * pendiente reapareciera para siempre, porque nunca habría un log con `date == plannedDate`.
 *
 * La lista resultante está en orden cronológico (el lunes primero), así que el pendiente más
 * antiguo es siempre el primero.
 *
 * Función pura: sin `Clock`, sin ViewModel, sin I/O — [today] se recibe desde fuera para que sea
 * trivial de revisar y de probar.
 */
fun pendingWorkoutsForWeek(
    routines: List<Routine>,
    logs: List<WorkoutLog>,
    today: LocalDate,
    dismissed: Set<String>,
): List<PendingWorkout> {
    val mondayOfWeek = today.minus(today.dayOfWeek.ordinal.toLong(), DateTimeUnit.DAY)
    val daysElapsedThisWeek = today.dayOfWeek.ordinal // LUN=0 … DOM=6: días ya pasados antes de hoy

    return (0 until daysElapsedThisWeek).flatMap { offset ->
        val plannedDate = mondayOfWeek.plus(offset.toLong(), DateTimeUnit.DAY)
        val plannedDateStr = plannedDate.toString()
        val dayAbbr = plannedDate.dayOfWeek.toSpanishAbbr()

        routines.routinesForDay(dayAbbr).mapNotNull { routine ->
            if (pendingWorkoutKey(plannedDateStr, routine.id) in dismissed) return@mapNotNull null

            val yaHecho = logs.any { log ->
                (log.date == plannedDateStr || log.recoveredFrom == plannedDateStr) &&
                    log.routineId == routine.id &&
                    log.completado
            }
            if (yaHecho) null else PendingWorkout(routine, plannedDateStr, dayAbbr)
        }
    }
}
