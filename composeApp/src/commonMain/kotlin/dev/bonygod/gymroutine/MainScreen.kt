package dev.bonygod.gymroutine

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import dev.bonygod.gymroutine.history.ui.screens.HistoryScreen
import dev.bonygod.gymroutine.home.ui.screens.HomeScreen
import dev.bonygod.gymroutine.profile.ui.screens.ProfileScreen
import dev.bonygod.gymroutine.routines.ui.screens.RoutinesScreen
import dev.bonygod.gymroutine.workout.ui.screens.WorkoutScreen

enum class BottomTab(val label: String, val icon: ImageVector) {
    Home("Inicio", Icons.Default.Home),
    Routines("Rutinas", Icons.Default.ViewList),
    Workout("Entreno", Icons.Default.FitnessCenter),
    History("Historial", Icons.Default.History),
    Profile("Perfil", Icons.Default.Person),
}

@Composable
fun MainScreen(userId: String = "") {
    var selectedTab by remember { mutableStateOf(BottomTab.Home) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                BottomTab.entries.forEach { tab ->
                    NavigationBarItem(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        icon = { Icon(tab.icon, contentDescription = tab.label) },
                        label = { Text(tab.label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.surfaceVariant,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        ),
                    )
                }
            }
        },
    ) { _ ->
        when (selectedTab) {
            BottomTab.Home -> HomeScreen()
            BottomTab.Routines -> RoutinesScreen()
            BottomTab.Workout -> WorkoutScreen()
            BottomTab.History -> HistoryScreen()
            BottomTab.Profile -> ProfileScreen(userId = userId)
        }
    }
}
