package org.bonygod.gymroutine.ui.view

import androidx.compose.runtime.Composable
import org.bonygod.gymroutine.ui.navigation.AppNavigation
import org.bonygod.gymroutine.ui.theme.CustomTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    CustomTheme {
        AppNavigation()
    }
}