package com.omar.retromp3recorder.bl.audio.actions

import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.audioplayer.PlayerStartOptions
import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.domain.FromToMillis
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.storage.repo.local.PlayerProgressRepo
import com.omar.retromp3recorder.utils.domain.toFromToMillis
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class StartPlaybackUC @Inject constructor(
    private val audioPlayer: AudioPlayer,
    private val currentFileRepo: CurrentFileRepo,
    private val playerProgressRepo: PlayerProgressRepo,
) {
    suspend fun execute() {
        val progress = requireNotNull(playerProgressRepo.flow().first().value) {
            "Progress must not be null"
        }
        val currentFileWrapper = currentFileRepo.first().value
        val existingFile = requireNotNull(currentFileWrapper as? ExistingFileWrapper) {
            "Expected ExistingFileWrapper, but was $currentFileWrapper"
        }

        val rangeActive = progress.range.settings.isActive
        val fromToMillis = if (rangeActive) {
            progress.range.toFromToMillis(progress.duration)
        } else {
            FromToMillis(from = progress.progress, to = progress.duration)
        }
        val relativeSeekPosition = (progress.progress - fromToMillis.from).coerceAtLeast(
            0L
        )
        val seekPosition = if (progress.progress > fromToMillis.to) 0 else relativeSeekPosition

        val options = PlayerStartOptions(
            isStopToRangeStartEnabled = rangeActive,
            filePath = existingFile.path,
            rangeMillis = fromToMillis,
            length = progress.duration,
            relativeSeekPosition = seekPosition
        )

        audioPlayer.onInput(AudioPlayer.Input.Start(options))
    }
}
