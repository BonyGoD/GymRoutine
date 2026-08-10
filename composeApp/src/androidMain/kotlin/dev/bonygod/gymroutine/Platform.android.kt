package dev.bonygod.gymroutine

import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Build

internal lateinit var appContext: Context
    private set

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.RELEASE}"

    // No requiere habilitar buildFeatures.buildConfig: el flag ya lo lleva el propio paquete instalado.
    override val isDebugBuild: Boolean
        get() = appContext.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
}

actual fun getPlatform(): Platform = AndroidPlatform()

fun initPlatform(context: Context) {
    appContext = context.applicationContext
}
