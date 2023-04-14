package com.omar.retromp3recorder.bl.database

import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.storage.db.AppDatabase
import com.omar.retromp3recorder.storage.db.toDatabaseEntity
import javax.inject.Inject

class DbUpdaterUCSuspend @Inject constructor(
    private val appDatabase: AppDatabase,
) {
    suspend fun execute(update: List<ExistingFileWrapper>) {
        if (update.isNotEmpty()) {
            appDatabase.fileEntityDao().update(update.map { it.toDatabaseEntity() })
        }
    }
}
