package org.bonygod.gymroutine

import android.app.Application
import org.bonygod.gymroutine.core.di.androidDatabaseModule
import org.bonygod.gymroutine.core.di.appModule
import org.bonygod.gymroutine.core.di.dataModule
import org.bonygod.gymroutine.core.di.initKoin
import org.bonygod.gymroutine.core.di.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level

class GymRoutineApp: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@GymRoutineApp)
            modules(androidDatabaseModule, appModule, viewModelsModule, dataModule)
        }
    }
}