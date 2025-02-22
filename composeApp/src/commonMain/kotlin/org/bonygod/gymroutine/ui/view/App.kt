package org.bonygod.gymroutine.ui.view

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import org.bonygod.gymroutine.ui.theme.CustomTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    CustomTheme {
            Navigator(screen = LoginOrSignup()){navigator ->
                SlideTransition(navigator)
            }
    }
}