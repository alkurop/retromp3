package com.omar.retromp3recorder.bl.crop

import com.omar.retromp3recorder.bl.actions.CropWithProductUC
import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.domain.NewNameSuggestion
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.utils.domain.toOptional
import javax.inject.Inject

class CropInPlaceUC @Inject constructor(
    private val cropOutsideUC: CropWithProductUC,
    private val currentFileRepo: CurrentFileRepo,
) {
    suspend fun execute(nameSuggestion: NewNameSuggestion): Result<ExistingFileWrapper> {
        return cropOutsideUC.execute(nameSuggestion)
            .onSuccess { file ->
                currentFileRepo.emit(file.toOptional())
            }
    }
}
