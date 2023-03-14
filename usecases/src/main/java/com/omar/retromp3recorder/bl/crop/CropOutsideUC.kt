package com.omar.retromp3recorder.bl.crop

import com.omar.retromp3recorder.bl.actions.CropUC
import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.domain.NewNameSuggestion
import com.omar.retromp3recorder.utils.platform.Optional
import javax.inject.Inject

class CropOutsideUC @Inject constructor(
    private val cropUC: CropUC,
) {
    fun execute(nameSuggestion: NewNameSuggestion): Optional<ExistingFileWrapper>{
        return cropUC
            .execute(nameSuggestion).blockingGet()
    }

}
