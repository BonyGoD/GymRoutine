package org.bonygod.gymroutine.core.di

import androidx.room.RoomDatabase
import org.bonygod.gymroutine.data.localDb.RoomDb
import org.bonygod.gymroutine.data.localDb.iosDatabaseBuilder
import org.koin.dsl.module


val iosDatabaseModule = module {
    single<RoomDatabase.Builder<RoomDb>> { iosDatabaseBuilder() }
}