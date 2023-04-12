package com.omar.retromp3recorder.bl.audio.actions

import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.storage.repo.local.PlayerControlsRepo
import javax.inject.Inject

class PlayerObserveSettingsUC @Inject constructor(
    private val audioPlayer: AudioPlayer,
    private val playerControlsRepo: PlayerControlsRepo
) {
    suspend fun execute() {
        playerControlsRepo.flow()
            .collect { settings -> audioPlayer.onInput(AudioPlayer.Input.ChangeControls(settings)) }
    }
}
