package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.dto.ExistingFileWrapper
import com.omar.retromp3recorder.storage.db.AppDatabase
import com.omar.retromp3recorder.storage.db.toDatabaseEntity
import com.omar.retromp3recorder.storage.repo.CurrentFileRepo
import com.omar.retromp3recorder.utils.FileDeleter
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class DeleteCurrentFileUC @Inject constructor(
    private val appDatabase: AppDatabase,
    private val currentFileRepo: CurrentFileRepo,
    private val fileDeleter: FileDeleter,
    private val takeLastFileUC: TakeLastFileFastUC
) {
    fun execute(): Completable {
        return currentFileRepo
            .takeOne()
            .flatMapCompletable { optional ->
                val file = (optional.value as? ExistingFileWrapper)

                Completable.fromAction {
                    file?.let {
                        fileDeleter.deleteFile(it.path)
                        appDatabase.fileEntityDao().delete(listOf(file.toDatabaseEntity()))
                    }

                }
            }
            .andThen(takeLastFileUC.execute())
    }
}

//todo fix take last file usecase