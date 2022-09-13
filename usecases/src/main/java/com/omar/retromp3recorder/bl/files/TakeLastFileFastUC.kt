package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.storage.db.AppDatabase
import com.omar.retromp3recorder.storage.db.toFileWrapper
import com.omar.retromp3recorder.storage.repo.CurrentFileRepo
import com.omar.retromp3recorder.utils.FileEmptyChecker
import com.omar.retromp3recorder.utils.Optional
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

/**
 * Runs the FindFilesUC
 *
 * Tasks the last file from file list repo
 * (we expect it to be the last recording)
 *
 * and puts it into the CurrentFileRepo
 */

//todo remove call to db
class TakeLastFileFastUC @Inject constructor(
    private val appDatabase: AppDatabase,
    private val fileEmptyChecker: FileEmptyChecker,
    private val currentFileRepo: CurrentFileRepo,
    private val scheduler: Scheduler
) {
    fun execute(): Completable = Completable
        .fromAction {
            val fileList =
                appDatabase.fileEntityDao().getAll().map { it.toFileWrapper() }.asReversed()
            val file = fileList.find { fileEmptyChecker.isFileEmpty(it.path).not() }
            currentFileRepo.onNext(Optional(file))
        }
        .subscribeOn(scheduler)
}
