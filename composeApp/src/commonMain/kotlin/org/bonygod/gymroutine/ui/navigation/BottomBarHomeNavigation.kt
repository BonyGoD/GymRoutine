package org.bonygod.gymroutine.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.bonygod.gymroutine.ui.view.components.BottomNavigationBarContent
import org.bonygod.gymroutine.ui.view.components.TopBarContent
import org.bonygod.gymroutine.ui.view.home.screens.DashboardScreen
import org.bonygod.gymroutine.ui.view.home.screens.ProfileScreen
import org.bonygod.gymroutine.ui.view.home.screens.RoutinesScreen
import org.bonygod.gymroutine.ui.view.viewModels.UserProfileViewModel
import org.koin.compose.viewmodel.koinViewModel

sealed class Tabs(val route: String, val icon: ImageVector?, val title: String) {
    data object TabDashboard : Tabs("dashboard", Icons.Rounded.Home, "Dashboard")
    data object TabRoutines : Tabs("routines", Icons.AutoMirrored.Rounded.List, "Routines")
    data object TabUserProfile : Tabs("userProfile", Icons.Rounded.Person, "User Profile")
}

@Composable
fun BottomBarHomeNavigation(
    userProfileViewModel: UserProfileViewModel = koinViewModel(),
    mainNavController: NavHostController
) {

    val navController = rememberNavController()
    val userData by userProfileViewModel.userData.collectAsState()

    Scaffold(
        bottomBar = {
            BottomNavigationBarContent(
                navController = navController
            )
        },
        topBar = {
            TopBarContent(userData?.userName.orEmpty())
        }
    ) { innerPadding ->
        NavHost(navController, startDestination = Tabs.TabDashboard.route) {
            addDashboardScreen(Modifier.padding(innerPadding), mainNavController, userProfileViewModel)
            addRoutinesScreen(Modifier.padding(innerPadding), mainNavController, userProfileViewModel)
            addUserProfileScreen(Modifier.padding(innerPadding), mainNavController, userProfileViewModel)
        }
    }
}

private fun NavGraphBuilder.addDashboardScreen(modifier: Modifier = Modifier, mainNavController: NavController, userProfileViewModel: UserProfileViewModel) {
    composable(Tabs.TabDashboard.route) {
        DashboardScreen(
            modifier,
            userProfileViewModel,
            navigateToRoutineScreen = {
                mainNavController.navigate("RoutineScreen")
            }
        )
    }
}

private fun NavGraphBuilder.addRoutinesScreen(
    modifier: Modifier = Modifier,
    mainNavController: NavController,
    userProfileViewModel: UserProfileViewModel
) {
    composable(Tabs.TabRoutines.route) {
        RoutinesScreen(
            modifier,
            userProfileViewModel,
            navigateToAddRoutinesScreen = {
                mainNavController.navigate("AddRoutinesScreen")
            },
        )
    }
}

private fun NavGraphBuilder.addUserProfileScreen(
    modifier: Modifier = Modifier,
    mainNavController: NavHostController,
    userProfileViewModel: UserProfileViewModel
) {
    composable(Tabs.TabUserProfile.route) {
        ProfileScreen(
            modifier,
            userProfileViewModel,
            navigateToLoginOrSignup = {
                mainNavController.navigate("LoginOrSignup") {
                    popUpTo(0) { inclusive = true }
                }
            }
        )
    }
}
