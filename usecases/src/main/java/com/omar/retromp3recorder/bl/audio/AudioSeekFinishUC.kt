package com.omar.retromp3recorder.bl.audio

import com.omar.retromp3recorder.audioplayer.AudioPlayer
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.rx3.asFlow
import javax.inject.Inject

class AudioSeekFinishUC @Inject constructor(
    private val audioPlayer: AudioPlayer,
    private val startPlaybackUC: StartPlaybackUC,
) {
    suspend fun execute() {
        when (audioPlayer.observeState().asFlow().first()) {
            AudioPlayer.State.Playing,
            AudioPlayer.State.PausedToSeek -> startPlaybackUC.execute().blockingAwait()
            AudioPlayer.State.Idle -> {
                //do nothing
            }
        }
    }
}
