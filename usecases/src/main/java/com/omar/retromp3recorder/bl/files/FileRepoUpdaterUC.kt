package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.dto.ExistingFileWrapper
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.utils.Optional
import com.omar.retromp3recorder.utils.toOptional
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class FileRepoUpdaterUC @Inject constructor(
    private val currentFileRepo: CurrentFileRepo
) {
    fun execute(update: List<ExistingFileWrapper>): Completable {
        return if (update.isEmpty()) {
            Completable.complete()
        } else Completable.fromAction {
            val currentFile = currentFileRepo.observe().blockingFirst()
            val value = currentFile.value
            if (value == null) {
                currentFileRepo.onNext(update.last().toOptional())
            } else {
                update.firstOrNull { it.path ==  value.path }?.let {
                    currentFileRepo.onNext(Optional(it))
                }
            }
        }
    }
}
