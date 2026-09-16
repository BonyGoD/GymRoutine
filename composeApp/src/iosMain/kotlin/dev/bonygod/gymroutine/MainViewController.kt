package dev.bonygod.gymroutine

import androidx.compose.ui.window.ComposeUIViewController
import dev.bonygod.admob.kmp.AdMobKMP
import dev.bonygod.admob.kmp.config.AdMobConfig
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    AdMobKMP.configure(AdMobConfig(interstitialEnabled = false))
    return ComposeUIViewController { App() }
}
