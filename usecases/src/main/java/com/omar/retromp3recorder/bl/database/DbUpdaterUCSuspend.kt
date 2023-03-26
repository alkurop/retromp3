package com.omar.retromp3recorder.bl.database

import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.storage.db.AppDatabase
import com.omar.retromp3recorder.storage.db.toDatabaseEntity
import com.omar.retromp3recorder.utils.platform.ScopeJobWrapper
import kotlinx.coroutines.launch
import javax.inject.Inject

class DbUpdaterUCSuspend @Inject constructor(
    private val appDatabase: AppDatabase,
    private val jobWrapper: ScopeJobWrapper
) {
    fun execute(update: List<ExistingFileWrapper>) {
        if (update.isNotEmpty()) {
            jobWrapper.launch {
                appDatabase.fileEntityDao().update(update.map { it.toDatabaseEntity() })
            }
        }
    }
}
