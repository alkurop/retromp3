package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.utils.platform.FileRenamer
import com.omar.retromp3recorder.utils.domain.repo.first
import javax.inject.Inject

class CanRenameNameUC @Inject constructor(
    private val currentFileRepo: CurrentFileRepo,
    private val fileRenamer: FileRenamer
) {
    suspend fun execute(
        path: String,
    ): Boolean {
        val currentFile = currentFileRepo.first().value
        return if (path.isNotEmpty() && currentFile != null && currentFile is ExistingFileWrapper) {
            fileRenamer.canRename(currentFile, path)
        } else false
    }
}
