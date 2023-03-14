package com.omar.retromp3recorder.bl.crop

import com.omar.retromp3recorder.bl.actions.CropUC
import com.omar.retromp3recorder.domain.NewNameSuggestion
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.utils.platform.toOptional
import javax.inject.Inject

class CropInPlaceUC @Inject constructor(
    private val cropOutsideUC: CropUC,
    private val currentFileRepo: CurrentFileRepo,
) {
    suspend fun execute(nameSuggestion: NewNameSuggestion) {
        cropOutsideUC.execute(nameSuggestion).value?.let { file ->
            currentFileRepo.emit(file.toOptional())
        }
    }
}
