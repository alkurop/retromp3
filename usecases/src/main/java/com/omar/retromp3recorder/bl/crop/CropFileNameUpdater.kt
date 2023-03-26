package com.omar.retromp3recorder.bl.crop

import com.omar.retromp3recorder.bl.files.GetCropFileNameUC
import com.omar.retromp3recorder.domain.NewNameSuggestion
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.utils.domain.Optional
import com.omar.retromp3recorder.utils.domain.toOptional
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CropFileNameUpdater @Inject constructor(
    private val currentFileRepo: CurrentFileRepo,
    private val cropGeneratorUC: GetCropFileNameUC
) {
    fun flow(): Flow<Optional<NewNameSuggestion>> {
        return currentFileRepo.flow()
            .map { file ->
                file.value?.let { fileWrapper ->
                    cropGeneratorUC
                        .execute(fileWrapper.path)
                        .toOptional()
                } ?: Optional.empty()
            }
    }
}
