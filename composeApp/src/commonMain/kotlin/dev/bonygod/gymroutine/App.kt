package dev.bonygod.gymroutine

import androidx.compose.runtime.Composable
import dev.bonygod.gymroutine.core.navigation.NavigationWrapper
import dev.bonygod.gymroutine.core.theme.GymRoutineTheme

@Composable
fun App() {
    GymRoutineTheme {
        NavigationWrapper()
    }
}
