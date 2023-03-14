package com.omar.retromp3recorder.bl.actions

import com.omar.retromp3recorder.bl.audio.JoinedProgressMapper
import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.domain.JoinedProgress
import com.omar.retromp3recorder.domain.NewNameSuggestion
import com.omar.retromp3recorder.io.audiotransformer.CropRequest
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.utils.domain.toFromToMillis
import javax.inject.Inject


class GatherCropRequestUC @Inject constructor(
    private val currentFileRepo: CurrentFileRepo,
    private val joinedProgress: JoinedProgressMapper
) {
    suspend fun execute(nameSuggestion: NewNameSuggestion): CropRequest {
        val currentFile = currentFileRepo.first()
        val progress = joinedProgress.observe().blockingFirst()
        val file = currentFile.value as ExistingFileWrapper
        val playerProgress = (progress as JoinedProgress.PlayerProgressShown).progress
        val range = playerProgress.range.toFromToMillis(playerProgress.duration)

        return CropRequest(
            range = range,
            original = file,
            newFileNameSuggestion = nameSuggestion
        )
    }
}
