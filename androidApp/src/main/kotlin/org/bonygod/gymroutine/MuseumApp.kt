package org.bonygod.gymroutine

import android.app.Application
import org.bonygod.gymroutine.di.initKoin

class MuseumApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }
}
