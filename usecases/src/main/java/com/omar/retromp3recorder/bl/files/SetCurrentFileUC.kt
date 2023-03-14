package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.utils.platform.Optional
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import javax.inject.Inject

class SetCurrentFileUC @Inject constructor(
    private val currentFileRepo: CurrentFileRepo,
) {
    suspend fun execute(file: ExistingFileWrapper) {
        currentFileRepo.emit(Optional(file))
    }
}
