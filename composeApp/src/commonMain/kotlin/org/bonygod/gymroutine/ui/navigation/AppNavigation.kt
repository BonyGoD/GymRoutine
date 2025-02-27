package org.bonygod.gymroutine.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
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
import org.bonygod.gymroutine.ui.view.viewModels.DialogViewModel

@Composable
fun AppNavigation() {

    val scope = rememberCoroutineScope()
    val auth = remember { Firebase.auth }
    val dialogViewModel: DialogViewModel = viewModel()

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
                navController.navigate("PrimeraPantalla") {
                    popUpTo("LoginOrSignup") { inclusive = true }
                }
            } else {
                LoginOrSignup(
                    loginClick = { navController.navigate("Login") },
                    signUpClick = { navController.navigate("SignUp") },
                    dialogViewModel = dialogViewModel,
                )
            }
        }

        composable("Login") {
            Login(
                dialogViewModel,
                auth,
                scope,
                navigateToForgotScreen = { navController.navigate("ForgotPassword") },
                navigateToPrimeraPantalla = {
                    navController.navigate("PrimeraPantalla") {
                        popUpTo("LoginOrSignup") { inclusive = true }
                    }
                }
            )
        }

        composable("SignUp") {
            SignUp(auth, scope)
        }

        composable("PrimeraPantalla") {
            PrimeraPantalla(auth, scope)
        }

        composable("ForgotPassword") {
            ForgotPassword(dialogViewModel, auth, scope) {
                navController.navigate("LoginOrSignup") {
                    popUpTo("ForgotPassword") { inclusive = true }
                }
            }
        }
    }
}