package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.dto.ExistingFileWrapper
import com.omar.retromp3recorder.utils.FileDeleter
import com.omar.retromp3recorder.utils.takeOneObservable
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class DeleteCurrentFileUC @Inject constructor(
    private val currentFileMapper: CurrentFileMapper,
    private val fileDeleter: FileDeleter,
    private val takeLastFileUC: TakeLastFileFastUC
) {
    fun execute(): Completable {
        return currentFileMapper
            .observe().takeOneObservable()
            .flatMapCompletable { optional ->
                val filePath = (optional.value as? ExistingFileWrapper)?.path

                Completable.fromAction {
                    filePath?.let { fileDeleter.deleteFile(filePath) }
                }
            }
            .andThen(takeLastFileUC.execute())
    }
}