package dev.bonygod.gymroutine.core.navigation

sealed class Routes {
    data object Login : Routes()

    data object ForgotPassword : Routes()

    data object Register : Routes()

    data class Main(
        val userId: String,
    ) : Routes()

    data object AddRoutine : Routes()

    data class EditRoutine(val routineId: String) : Routes()
}
