package org.bonygod.gymroutine

import androidx.compose.ui.window.ComposeUIViewController
import org.bonygod.gymroutine.core.di.initKoin
import org.bonygod.gymroutine.ui.view.App

fun MainViewController() = ComposeUIViewController(
    configure = { initKoin () }
) {
    App()
}