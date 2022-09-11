package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.dto.ExistingFileWrapper
import com.omar.retromp3recorder.storage.db.AppDatabase
import com.omar.retromp3recorder.storage.db.toDatabaseEntity
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class DbUpdaterUC @Inject constructor(
    private val appDatabase: AppDatabase
) {
    fun execute(update: List<ExistingFileWrapper>): Completable {
        return if (update.isEmpty()) {
            Completable.complete()
        } else
            Completable.fromAction {
                appDatabase.fileEntityDao().update(update.map { it.toDatabaseEntity() })
            }
    }
}