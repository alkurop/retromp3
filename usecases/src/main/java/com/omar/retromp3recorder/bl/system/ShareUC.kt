package com.omar.retromp3recorder.bl.system

import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.share.Sharer
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import java.io.File
import javax.inject.Inject

class ShareUC @Inject constructor(
    private val sharingModule: Sharer,
    private val currentFileRepo: CurrentFileRepo
) {
    suspend fun execute() {
        val fileWrapper = currentFileRepo.first().value
        val file = requireNotNull(fileWrapper as? ExistingFileWrapper) {
            "Current file should be Existing, but was $fileWrapper"
        }
        sharingModule.share(File(file.path))
    }
}
