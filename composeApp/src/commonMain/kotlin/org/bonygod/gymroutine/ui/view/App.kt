package org.bonygod.gymroutine.ui.view

import androidx.compose.runtime.Composable
import org.bonygod.gymroutine.ui.navigation.AppNavigation
import org.bonygod.gymroutine.ui.theme.CustomTheme
import org.bonygod.gymroutine.ui.view.viewModels.UserViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    CustomTheme {
            AppNavigation()
    }
}