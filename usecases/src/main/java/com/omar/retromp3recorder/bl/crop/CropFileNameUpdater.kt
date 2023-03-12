package com.omar.retromp3recorder.bl.crop

import com.omar.retromp3recorder.bl.files.GetCropFileNameUC
import com.omar.retromp3recorder.domain.NewNameSuggestion
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.utils.platform.Optional
import com.omar.retromp3recorder.utils.platform.toOptional
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class CropFileNameUpdater @Inject constructor(
    private val currentFileRepo: CurrentFileRepo,
    private val cropGeneratorUC: GetCropFileNameUC
) {
    fun observe(): Observable<Optional<NewNameSuggestion>> = currentFileRepo
        .observe()
        .flatMapSingle {
            it.value?.let { fileWrapper ->
                cropGeneratorUC
                    .execute(fileWrapper.path)
                    .map { suggestedName -> suggestedName.toOptional() }
            } ?: Single.just(Optional.empty())
        }
}
