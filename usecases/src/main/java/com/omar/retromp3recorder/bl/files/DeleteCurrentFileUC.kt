package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.storage.db.AppDatabase
import com.omar.retromp3recorder.storage.db.toDatabaseEntity
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.utils.platform.FileDeleter
import javax.inject.Inject

class DeleteCurrentFileUC @Inject constructor(
    private val appDatabase: AppDatabase,
    private val currentFileRepo: CurrentFileRepo,
    private val fileDeleter: FileDeleter,
    private val takeLastFileUC: TakeLastFileDbItemUC
) {
    suspend fun execute() {
        val existingFileWrapper = currentFileRepo.first().value as? ExistingFileWrapper
        existingFileWrapper?.let { file ->
            fileDeleter.deleteFile(file.path)
            appDatabase.fileEntityDao().delete(listOf(file.toDatabaseEntity()))

            val lastFile = takeLastFileUC.get().blockingGet()
            currentFileRepo.emit(lastFile)
        }
    }
}
