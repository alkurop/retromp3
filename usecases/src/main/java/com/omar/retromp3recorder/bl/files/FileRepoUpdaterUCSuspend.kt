package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.utils.domain.Optional
import com.omar.retromp3recorder.utils.domain.repo.first
import com.omar.retromp3recorder.utils.domain.toOptional
import javax.inject.Inject

class FileRepoUpdaterUCSuspend @Inject constructor(
    private val currentFileRepo: CurrentFileRepo
) {
    suspend fun execute(update: List<ExistingFileWrapper>) {
        if (update.isNotEmpty()) {
            val currentFile = currentFileRepo.first()
            val value = currentFile.value
            if (value == null) {
                currentFileRepo.emit(update.last().toOptional())
            } else {
                update.firstOrNull { it.path == value.path }?.let {
                    currentFileRepo.emit(Optional(it))
                }
            }
        }
    }
}
