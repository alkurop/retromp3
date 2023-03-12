package com.omar.retromp3recorder.storage.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase

interface AppDatabase {
    fun fileEntityDao(): FileDbEntityDao
}

@Database(
    entities = [FileDbEntity::class],
    version = 5,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 1, to = 3),
        AutoMigration(from = 1, to = 4),
        AutoMigration(from = 1, to = 5),

        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 2, to = 4),
        AutoMigration(from = 2, to = 5),

        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 3, to = 5),

        AutoMigration(from = 4, to = 5),
    ]
)
abstract class AppDatabaseImpl : RoomDatabase(), AppDatabase


