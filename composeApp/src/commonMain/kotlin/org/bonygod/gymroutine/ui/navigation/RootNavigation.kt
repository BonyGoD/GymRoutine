package org.bonygod.gymroutine.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.bonygod.gymroutine.ui.view.components.BottomNavigationBarContent
import org.bonygod.gymroutine.ui.view.homeScreens.DashboardScreen
import org.bonygod.gymroutine.ui.view.homeScreens.ProfileScreen
import org.bonygod.gymroutine.ui.view.homeScreens.RoutinesScreen

sealed class RootScreen(val route: String, val icon: ImageVector?, val title: String) {
    data object TabDashboard : RootScreen("dashboard", Icons.Rounded.Home, "Dashboard")
    data object TabRoutines : RootScreen("routines", Icons.AutoMirrored.Rounded.List, "Routines")
    data object TabUserProfile : RootScreen("userProfile", Icons.Rounded.Person, "User Profile")
}

@Composable
fun RootNavigation(
    navController: NavHostController
) {
    Scaffold(
        bottomBar = {
            BottomNavigationBarContent(
                navController = navController
            )
        }
    ) { innerPadding ->
        NavHost(navController, startDestination = RootScreen.TabDashboard.route) {
            addDashboardScreen(Modifier.padding(innerPadding))
            addRoutinesScreen(Modifier.padding(innerPadding))
            addUserProfileScreen(Modifier.padding(innerPadding))
        }
    }
}

private fun NavGraphBuilder.addDashboardScreen(modifier: Modifier = Modifier) {
    composable(RootScreen.TabDashboard.route) {
        DashboardScreen(modifier)
    }
}

private fun NavGraphBuilder.addRoutinesScreen(modifier: Modifier = Modifier) {
    composable(RootScreen.TabRoutines.route) {
        RoutinesScreen(modifier)
    }
}

private fun NavGraphBuilder.addUserProfileScreen(modifier: Modifier = Modifier) {
    composable(RootScreen.TabUserProfile.route) {
        ProfileScreen(modifier)
    }
}
