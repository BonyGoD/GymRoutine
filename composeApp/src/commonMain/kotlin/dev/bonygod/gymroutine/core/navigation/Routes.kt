package dev.bonygod.gymroutine.core.navigation

sealed class Routes {
    data object Login : Routes()

    data object ForgotPassword : Routes()

    data object Register : Routes()

    data class CompleteProfile(
        val userId: String,
    ) : Routes()

    data class Main(
        val userId: String,
    ) : Routes()

    data object AddRoutine : Routes()

    data class EditRoutine(val routineId: String) : Routes()

    data class Workout(
        val routineId: String = "",
        val routineName: String = "",
        /** Fecha ISO-8601 "YYYY-MM-DD" del día planificado que se recupera, o null si no lo es. */
        val recoveredFrom: String? = null,
    ) : Routes()
}
