package org.bonygod.gymroutine.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import org.bonygod.gymroutine.ui.view.PrimeraPantalla
import org.bonygod.gymroutine.ui.view.loginScreens.ForgotPassword
import org.bonygod.gymroutine.ui.view.loginScreens.Login
import org.bonygod.gymroutine.ui.view.loginScreens.LoginOrSignup
import org.bonygod.gymroutine.ui.view.loginScreens.SignUp

@Composable
fun AppNavigation() {

    val scope = rememberCoroutineScope()
    val auth = remember { Firebase.auth }
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "LoginOrSignup"
    ) {

        composable(
            route = "LoginOrSignup",
            arguments = listOf(navArgument("titleDialog") { nullable = true })
        ) {
            if (auth.currentUser != null) {
                navController.navigate("PrimeraPantalla/{user}") {
                    popUpTo("LoginOrSignup") { inclusive = true }
                }
            } else {
                LoginOrSignup(
                    loginClick = { navController.navigate("Login") },
                    signUpClick = { navController.navigate("SignUp") }
                )
            }
        }

        composable("Login") {
            Login(
                navigateToForgotScreen = { navController.navigate("ForgotPassword") },
                navigateToPrimeraPantalla = { user ->
                    navController.navigate("PrimeraPantalla/$user") {
                        popUpTo("LoginOrSignup") { inclusive = true }
                    }
                }
            )
        }

        composable("SignUp") {
            SignUp(
                navigateToPrimeraPantalla = { user ->
                    navController.navigate("PrimeraPantalla/$user") {
                        popUpTo("LoginOrSignup") { inclusive = true }
                    }
                }
            )
        }

        composable("ForgotPassword") {
            ForgotPassword(auth) {
                navController.navigate("LoginOrSignup") {
                    popUpTo("LoginOrSignup") { inclusive = true }
                }
            }
        }

        composable("PrimeraPantalla/{user}") {backStackEntry ->
            val user = backStackEntry.arguments?.getString("user") ?: ""
            PrimeraPantalla(auth, scope, user)
        }
    }
}