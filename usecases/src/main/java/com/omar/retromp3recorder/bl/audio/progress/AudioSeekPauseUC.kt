package com.omar.retromp3recorder.bl.audio.progress

import com.omar.retromp3recorder.audioplayer.AudioPlayer
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AudioSeekPauseUC @Inject constructor(
    private val audioPlayer: AudioPlayer
) {
    suspend fun execute() {
        if (audioPlayer.stateFlow().first() == AudioPlayer.State.Playing) {
            audioPlayer.onInput(AudioPlayer.Input.SeekPause)
        }
    }
}
