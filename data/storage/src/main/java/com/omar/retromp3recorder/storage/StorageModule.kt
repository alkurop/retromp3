package com.omar.retromp3recorder.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.Keep
import androidx.room.Room
import com.omar.retromp3recorder.storage.db.AppDatabase
import com.omar.retromp3recorder.storage.db.AppDatabaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class StorageModule {
    @Provides
    fun provideSharedPrefs(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(DbKey.SETTINGS_NAME, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
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
