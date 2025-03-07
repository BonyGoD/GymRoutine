package org.bonygod.gymroutine.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import org.bonygod.gymroutine.data.model.User
import org.bonygod.gymroutine.ui.view.userProfileScreens.PrimeraPantalla
import org.bonygod.gymroutine.ui.view.loginScreens.ForgotPassword
import org.bonygod.gymroutine.ui.view.loginScreens.Login
import org.bonygod.gymroutine.ui.view.loginScreens.LoginOrSignup
import org.bonygod.gymroutine.ui.view.loginScreens.SignUp
import org.bonygod.gymroutine.ui.view.viewModels.UserViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    val userViewModel = koinViewModel<UserViewModel>()
    var user by remember { mutableStateOf<User?>(null) }
    var showScreen by remember { mutableStateOf<Boolean>(false) }

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
        startDestination = if (user != null) "PrimeraPantalla" else "LoginOrSignup"
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
                navigateToPrimeraPantalla = {
                    navController.navigate("PrimeraPantalla") {
                        popUpTo("LoginOrSignup") { inclusive = true }
                    }
                }
            )
        }

        composable("SignUp") {
            SignUp(
                navigateToPrimeraPantalla = {
                    navController.navigate("PrimeraPantalla") {
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

        composable("PrimeraPantalla") {
            PrimeraPantalla(
                navigateToLoginOrSignup = {
                    navController.navigate("LoginOrSignup") {
                        popUpTo("PrimeraPantalla") { inclusive = true }
                    }
                }
            )
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator()
    }
}