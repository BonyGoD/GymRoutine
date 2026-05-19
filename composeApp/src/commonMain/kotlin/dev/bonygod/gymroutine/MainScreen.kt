package dev.bonygod.gymroutine

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bonygod.gymroutine.core.navigation.Navigator
import dev.bonygod.gymroutine.history.ui.screens.HistoryScreen
import dev.bonygod.gymroutine.home.ui.screens.HomeScreen
import dev.bonygod.gymroutine.profile.ui.screens.ProfileScreen
import dev.bonygod.gymroutine.routines.ui.screens.RoutinesScreen
import org.koin.compose.koinInject

enum class BottomTab(val label: String, val icon: ImageVector) {
    Home("Inicio", Icons.Default.Home),
    Routines("Rutinas", Icons.AutoMirrored.Filled.ViewList),
    History("Historial", Icons.Default.History),
    Profile("Perfil", Icons.Default.Person),
}

@Composable
fun MainScreen(userId: String = "", navigator: Navigator = koinInject()) {
    var selectedTab by navigator.currentTab
    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        containerColor = colorScheme.background,
        bottomBar = {
            NavigationBar(
                containerColor = colorScheme.surface,
                tonalElevation = 0.dp,
            ) {
                BottomTab.entries.forEach { tab ->
                    NavigationBarItem(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        icon = {
                            Icon(
                                tab.icon,
                                contentDescription = tab.label,
                                modifier = Modifier.padding(bottom = 2.dp),
                            )
                        },
                        label = {
                            Text(
                                text = tab.label,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = (0.96).sp,
                            )
                        },
                        colors =
                        NavigationBarItemDefaults.colors(
                            selectedIconColor = colorScheme.primary,
                            selectedTextColor = colorScheme.primary,
                            indicatorColor = colorScheme.surfaceVariant,
                            unselectedIconColor = colorScheme.onSurfaceVariant,
                            unselectedTextColor = colorScheme.onSurfaceVariant,
                        ),
                    )
                }
            }
        },
    ) { _ ->
        when (selectedTab) {
            BottomTab.Home -> HomeScreen()
            BottomTab.Routines -> RoutinesScreen()
            BottomTab.History -> HistoryScreen()
            BottomTab.Profile -> ProfileScreen(userId = userId)
        }
    }
}
