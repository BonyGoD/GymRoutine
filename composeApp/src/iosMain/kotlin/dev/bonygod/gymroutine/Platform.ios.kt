package dev.bonygod.gymroutine

import platform.UIKit.UIDevice

class IOSPlatform : Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion

    // kotlin.native.Platform, no la interfaz Platform de este fichero — de ahí la referencia completa.
    override val isDebugBuild: Boolean
        get() = kotlin.native.Platform.isDebugBinary
}

actual fun getPlatform(): Platform = IOSPlatform()
