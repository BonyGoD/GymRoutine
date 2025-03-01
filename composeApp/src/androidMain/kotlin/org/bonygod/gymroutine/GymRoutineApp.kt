package org.bonygod.gymroutine

import android.app.Application
import org.bonygod.gymroutine.core.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level

class GymRoutineApp: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@GymRoutineApp)
        }
    }
}