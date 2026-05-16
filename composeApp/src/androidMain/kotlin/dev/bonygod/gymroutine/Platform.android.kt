package dev.bonygod.gymroutine

import android.content.Context
import android.os.Build

internal lateinit var appContext: Context
    private set

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.RELEASE}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

fun initPlatform(context: Context) {
    appContext = context.applicationContext
}
