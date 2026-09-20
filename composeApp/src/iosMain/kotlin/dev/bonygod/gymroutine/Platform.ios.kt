package dev.bonygod.gymroutine

import platform.UIKit.UIDevice
import kotlin.experimental.ExperimentalNativeApi

class IOSPlatform : Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion

    // kotlin.native.Platform, no la interfaz Platform de este fichero — de ahí la referencia completa.
    @OptIn(ExperimentalNativeApi::class)
    override val isDebugBuild: Boolean
        get() = kotlin.native.Platform.isDebugBinary

    override val adsEnabled: Boolean = true
}

actual fun getPlatform(): Platform = IOSPlatform()
