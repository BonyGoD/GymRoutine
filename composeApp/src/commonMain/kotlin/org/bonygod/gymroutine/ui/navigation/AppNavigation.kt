package org.bonygod.gymroutine.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import org.bonygod.gymroutine.data.model.User
import org.bonygod.gymroutine.ui.view.components.LoadingScreen
import org.bonygod.gymroutine.ui.view.loginScreens.ForgotPassword
import org.bonygod.gymroutine.ui.view.loginScreens.Login
import org.bonygod.gymroutine.ui.view.loginScreens.LoginOrSignup
import org.bonygod.gymroutine.ui.view.loginScreens.SignUp
import org.bonygod.gymroutine.ui.view.userProfileScreens.UserProfile
import org.bonygod.gymroutine.ui.view.userProfileScreens.Wellcome
import org.bonygod.gymroutine.ui.view.viewModels.UserViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val navHostController = rememberNavController()
    val userViewModel = koinViewModel<UserViewModel>()
    var user by remember { mutableStateOf<User?>(null) }
    var showScreen by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(500)
        user = userViewModel.getUser().first()
        showScreen = true
    }

    if (!showScreen) {
        LoadingScreen()
        return
    }

    NavHost(
        navController = navController,
        startDestination = if (user != null) "Wellcome" else "LoginOrSignup"
    ) {

        composable(
            route = "LoginOrSignup",
            arguments = listOf(navArgument("titleDialog") { nullable = true })
        ) {
            LoginOrSignup(
                loginClick = { navController.navigate("Login") },
                signUpClick = { navController.navigate("SignUp") }
            )
        }

        composable("Login") {
            Login(
                navigateToForgotScreen = { navController.navigate("ForgotPassword") },
                navigateToWellcome = {
                    navController.navigate("Wellcome") {
                        popUpTo("LoginOrSignup") { inclusive = true }
                    }
                }
            )
        }

        composable("SignUp") {
            SignUp(
                navigateToWellcome = {
                    navController.navigate("Wellcome") {
                        popUpTo("LoginOrSignup") { inclusive = true }
                    }
                }
            )
        }

        composable("ForgotPassword") {
            ForgotPassword {
                navController.navigate("LoginOrSignup") {
                    popUpTo("LoginOrSignup") { inclusive = true }
                }
            }
        }

        composable("Wellcome") {
            Wellcome(
                navigateToUserProfile = {
                    navController.navigate("UserProfile") {
                        popUpTo("Wellcome") { inclusive = false }
                    }
                },
                navigateToDashboard = {
                    navController.navigate("BottomBarHomeNavigation") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable("UserProfile") {
            UserProfile(
                navigateToDashboard = {
                    navController.navigate("BottomBarHomeNavigation") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable("BottomBarHomeNavigation") {
            BottomBarHomeNavigation(navController, navHostController)
        }
    }
}