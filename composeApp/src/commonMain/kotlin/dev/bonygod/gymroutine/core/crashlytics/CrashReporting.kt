package dev.bonygod.gymroutine.core.crashlytics

import dev.bonygod.crashlytics.kmp.core.CrashlyticsKMP

/**
 * Hoy los errores controlados —los `onFailure` de los repositorios— se convierten en un snackbar
 * y desaparecen: no queda ningún rastro para diagnosticarlos después del hecho. Esta extensión se
 * encadena igual que `.mapError()` y deja el fallo registrado en Crashlytics como non-fatal, sin
 * alterar el flujo ni el valor: no transforma ni se traga el [Result], solo lo observa de paso.
 *
 * [operation] identifica qué operación falló (p. ej. `"RoutineRepository.getRoutines"`); [keys]
 * añade contexto extra como custom keys de ese reporte.
 */
fun <T> Result<T>.reportFailure(operation: String, keys: Map<String, Any> = emptyMap()): Result<T> =
    onFailure { throwable -> CrashlyticsKMP.reporter.recordException(throwable, operation, keys) }
