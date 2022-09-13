package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.dto.ExistingFileWrapper
import com.omar.retromp3recorder.storage.db.AppDatabase
import com.omar.retromp3recorder.storage.db.toFileWrapper
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

/**
 * Maps current file filepath to database entity
 */
class FileDataGetter @Inject constructor(
    private val database: AppDatabase,
    private val scheduler: Scheduler
) {
    fun get(filePath: String): Single<ExistingFileWrapper> {
        return Single
            .fromCallable {
                database.fileEntityDao()
                    .getByFilepath(filePath).firstOrNull()!!.toFileWrapper()

            }
            .subscribeOn(scheduler)
    }
}