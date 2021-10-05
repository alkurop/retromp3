package com.omar.retromp3recorder.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.Keep
import androidx.room.Room
import com.omar.retromp3recorder.storage.db.AppDatabase
import com.omar.retromp3recorder.storage.db.AppDatabaseImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class StorageModule {
    @Provides
    fun provideSharedPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(DbKey.SETTINGS_NAME, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabaseImpl::class.java, DbKey.DB_NAME
        ).build()
    }
}

@Keep
object DbKey {
      const val DB_NAME = "database-name"
      const val SETTINGS_NAME = "app"
}