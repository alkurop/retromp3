package com.omar.retromp3recorder.bl.crop

import com.omar.retromp3recorder.bl.actions.CropUC
import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.domain.NewNameSuggestion
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.utils.platform.Optional
import com.omar.retromp3recorder.utils.platform.toOptional
import javax.inject.Inject

class CropInPlaceUC @Inject constructor(
    private val cropOutsideUC: CropUC,
    private val currentFileRepo: CurrentFileRepo,
) {
    suspend fun execute(nameSuggestion: NewNameSuggestion): Optional<ExistingFileWrapper> {
        val result = cropOutsideUC.execute(nameSuggestion).value?.also { file ->
            currentFileRepo.emit(file.toOptional())
        }
        return result.toOptional()
    }
}
