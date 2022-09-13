package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.dto.ExistingFileWrapper
import com.omar.retromp3recorder.storage.repo.CurrentFileRepo
import com.omar.retromp3recorder.storage.repo.FileListRepo
import com.omar.retromp3recorder.utils.Optional
import com.omar.retromp3recorder.utils.takeOne
import com.omar.retromp3recorder.utils.toOptional
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class FileRepoUpdaterUC @Inject constructor(
    private val fileListRepo: FileListRepo,
    private val currentFileRepo: CurrentFileRepo
) {
    fun execute(update: List<ExistingFileWrapper>): Completable {
        return if (update.isEmpty()) {
            Completable.complete()
        } else fileListRepo.observe()
            .takeOne()
            .flatMapCompletable { existingList ->
                if (existingList.isNotEmpty()) {
                    Completable.fromAction {
                        val updatedList = existingList.map { file ->
                            update.firstOrNull { it.path == file.path } ?: file
                        }
                        fileListRepo.onNext(updatedList)
                    }
                } else {
                    Completable.fromAction {
                        fileListRepo.onNext(update)
                    }
                }.andThen(Completable.fromAction {
                    val currentFile = currentFileRepo.observe().blockingFirst()
                    if (currentFile.value == null) {
                        currentFileRepo.onNext(update.last().toOptional())
                    }
                    update.firstOrNull { it.path == currentFile.value?.path }?.let {
                        currentFileRepo.onNext(Optional(it))
                    }
                }).andThen(Completable.complete())
            }
    }
}
