package org.bonygod.gymroutine.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import org.bonygod.gymroutine.ui.view.Login
import org.bonygod.gymroutine.ui.view.LoginOrSignup
import org.bonygod.gymroutine.ui.view.SignUp

lateinit var auth: FirebaseAuth

@Composable
fun AppNavigation() {

    auth = Firebase.auth

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "LoginOrSignup") {

        composable("LoginOrSignup") {
            LoginOrSignup(
                loginClick = { navController.navigate("Login") },
                signUpClick = { navController.navigate("SignUp") }
            )
        }

        composable("Login") {
            Login()
        }

        composable("SignUp") {
            SignUp()
        }
    }
}