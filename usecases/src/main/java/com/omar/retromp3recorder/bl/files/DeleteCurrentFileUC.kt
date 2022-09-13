package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.dto.ExistingFileWrapper
import com.omar.retromp3recorder.storage.repo.CurrentFileRepo
import com.omar.retromp3recorder.utils.FileDeleter
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class DeleteCurrentFileUC @Inject constructor(
    private val currentFileRepo: CurrentFileRepo,
    private val fileDeleter: FileDeleter,
    private val takeLastFileUC: TakeLastFileFastUC
) {
    fun execute(): Completable {
        return currentFileRepo
            .takeOne()
            .flatMapCompletable { optional ->
                val filePath = (optional.value as? ExistingFileWrapper)?.path

                Completable.fromAction {
                    filePath?.let { fileDeleter.deleteFile(filePath) }
                }
            }
            .andThen(takeLastFileUC.execute())
    }
}