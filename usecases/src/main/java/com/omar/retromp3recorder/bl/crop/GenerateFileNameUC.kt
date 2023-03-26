package com.omar.retromp3recorder.bl.crop

import com.omar.retromp3recorder.bl.files.GetCropFileNameUC
import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.domain.NewNameSuggestion
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.utils.platform.repo.first
import javax.inject.Inject

class GenerateFileNameUC @Inject constructor(
    private val currentFileRepo: CurrentFileRepo,
    private val cropGeneratorUC: GetCropFileNameUC
) {
    suspend fun execute(): NewNameSuggestion {
        val first = currentFileRepo.first()
        val fileWrapper = requireNotNull(first.value as? ExistingFileWrapper) {
            "Current file is not existing, but $first"
        }
        return cropGeneratorUC
            .execute(fileWrapper.path)
    }
}
