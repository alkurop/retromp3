package com.omar.retromp3recorder.bl.actions

import com.omar.retromp3recorder.bl.audio.JoinedProgressMapper
import com.omar.retromp3recorder.domain.CropRequest
import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.domain.JoinedProgress
import com.omar.retromp3recorder.domain.NewNameSuggestion
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.utils.domain.toFromToMillis
import javax.inject.Inject


class GatherCropRequestUC @Inject constructor(
    private val currentFileRepo: CurrentFileRepo,
    private val joinedProgress: JoinedProgressMapper
) {
    suspend fun execute(nameSuggestion: NewNameSuggestion): CropRequest {
        val file = requireNotNull(currentFileRepo.first().value as? ExistingFileWrapper) {
            "No current file present"
        }

        val playerState = joinedProgress.observe().blockingFirst()

        val playerProgress =
            requireNotNull(
                joinedProgress.observe().blockingFirst() as? JoinedProgress.PlayerProgressShown
            ) {
                "App not in player state, but in $playerState"
            }.progress

        val range = playerProgress.range.toFromToMillis(playerProgress.duration)

        return CropRequest(
            range = range,
            original = file,
            newFileNameSuggestion = nameSuggestion
        )
    }
}
