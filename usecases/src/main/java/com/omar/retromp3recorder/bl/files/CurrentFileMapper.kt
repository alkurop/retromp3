package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.dto.FileWrapper
import com.omar.retromp3recorder.dto.toFutureFileWrapper
import com.omar.retromp3recorder.storage.db.AppDatabase
import com.omar.retromp3recorder.storage.db.toFileWrapper
import com.omar.retromp3recorder.storage.repo.CurrentFileRepo
import com.omar.retromp3recorder.utils.Optional
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

/**
 * Maps current file filepath to database entity
 */
class CurrentFileMapper @Inject constructor(
    private val currentFileRepo: CurrentFileRepo,
    private val database: AppDatabase,
    private val scheduler: Scheduler
) {
    fun observe(): Observable<Optional<FileWrapper>> {
        return currentFileRepo
            .observe()
            .switchMap {
                val currentFilePath = it.value
                val currentFile = currentFilePath?.let {
                    database.fileEntityDao()
                        .getByFilepath(currentFilePath).firstOrNull()
                        ?.toFileWrapper()
                } ?: currentFilePath?.toFutureFileWrapper()
                Observable.just(Optional(currentFile))
            }
            .subscribeOn(scheduler)
            .share()
    }
}