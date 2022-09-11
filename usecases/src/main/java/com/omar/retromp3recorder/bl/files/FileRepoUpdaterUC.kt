package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.dto.ExistingFileWrapper
import com.omar.retromp3recorder.storage.repo.FileListRepo
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class FileRepoUpdaterUC @Inject constructor(
    private val fileRepo: FileListRepo
) {
    fun execute(update: List<ExistingFileWrapper>): Completable {
        return if (update.isEmpty()) {
            Completable.complete()
        } else if (fileRepo.hasNext()) {
            Completable.fromAction {
                val existingList = fileRepo.observe().blockingFirst()
                val updatedList = existingList.map { file ->
                    update.firstOrNull { it.path == file.path } ?: file
                }
                fileRepo.onNext(updatedList)
            }
        } else {
            Completable.fromAction { fileRepo.onNext(update) }
        }
    }
}