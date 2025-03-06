package org.bonygod.gymroutine.data.localDb

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

fun androidDatabaseBuilder(context: Context): RoomDatabase.Builder<RoomDb> {

    val dbFile = context.applicationContext.getDatabasePath("room_db.db")

    return Room.databaseBuilder(
        context,
        dbFile.absolutePath
    )
}