package dev.bonygod.gymroutine.core.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import dev.bonygod.gymroutine.MainScreen
import dev.bonygod.gymroutine.auth.ui.screens.ForgotPasswordScreen
import dev.bonygod.gymroutine.auth.ui.screens.LoginScreen
import dev.bonygod.gymroutine.auth.ui.screens.RegisterScreen
import org.koin.compose.koinInject

@Composable
fun NavigationWrapper() {
    val navigator: Navigator = koinInject()

    NavDisplay(
        backStack = navigator.backStack,
        onBack = { navigator.goBack() },
        entryProvider = entryProvider {
            entry<Routes.Login> {
                LoginScreen()
            }
            entry<Routes.ForgotPassword> {
                ForgotPasswordScreen()
            }
            entry<Routes.Register> {
                RegisterScreen()
            }
            entry<Routes.Main> { entry ->
                MainScreen(userId = entry.userId)
            }
        },
        transitionSpec = {
            slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(250)) togetherWith
                slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(250))
        },
        popTransitionSpec = {
            slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(250)) togetherWith
                slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(250))
        },
        predictivePopTransitionSpec = {
            slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(250)) togetherWith
                slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(250))
        },
    )
}
