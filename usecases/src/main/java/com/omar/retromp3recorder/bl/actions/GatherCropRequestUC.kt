package com.omar.retromp3recorder.bl.actions

import com.omar.retromp3recorder.bl.audio.JoinedProgressMapper
import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.domain.JoinedProgress
import com.omar.retromp3recorder.domain.NewNameSuggestion
import com.omar.retromp3recorder.io.audiotransformer.CropRequest
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.utils.takeOne
import com.omar.retromp3recorder.utils.toFromToMillis
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject


class GatherCropRequestUC @Inject constructor(
    private val currentFileRepo: CurrentFileRepo,
    private val joinedProgress: JoinedProgressMapper
) {
    fun execute(nameSuggestion: NewNameSuggestion): Single<CropRequest> {
        return Single.zip(
            currentFileRepo.takeOne(),
            joinedProgress.observe().takeOne()
        ) { currentFile, progress ->
            val file = currentFile.value as ExistingFileWrapper
            val playerProgress = (progress as JoinedProgress.PlayerProgressShown).progress
            val range = playerProgress.range.toFromToMillis(playerProgress.duration)

            CropRequest(
                range = range,
                original = file,
                newFileNameSuggestion = nameSuggestion
            )
        }
    }
}
