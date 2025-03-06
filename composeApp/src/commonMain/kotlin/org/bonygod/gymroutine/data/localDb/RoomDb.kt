package org.bonygod.gymroutine.data.localDb

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import org.bonygod.gymroutine.data.dao.UserDao
import org.bonygod.gymroutine.data.model.User

@Database(
    entities = [
        User::class
    ],
    version = 1,
    exportSchema = true
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class RoomDb: RoomDatabase() {
    abstract fun userDao(): UserDao

}
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<RoomDb> {
    override fun initialize(): RoomDb
}