package org.bonygod.gymroutine.core.di

import androidx.room.RoomDatabase
import org.bonygod.gymroutine.data.localDb.RoomDb
import org.bonygod.gymroutine.data.localDb.androidDatabaseBuilder
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val androidDatabaseModule = module {
    single<RoomDatabase.Builder<RoomDb>> { androidDatabaseBuilder(androidContext()) }
}