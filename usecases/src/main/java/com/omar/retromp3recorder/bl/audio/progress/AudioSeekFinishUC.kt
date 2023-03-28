package com.omar.retromp3recorder.bl.audio.progress

import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.bl.audio.actions.StartPlaybackUC
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AudioSeekFinishUC @Inject constructor(
    private val audioPlayer: AudioPlayer,
    private val startPlaybackUC: StartPlaybackUC,
) {
    suspend fun execute() {
        when (audioPlayer.stateFlow().first()) {
            AudioPlayer.State.Playing,
            AudioPlayer.State.PausedToSeek -> startPlaybackUC.execute()
            AudioPlayer.State.Idle -> {
                //do nothing
            }
        }
    }
}
