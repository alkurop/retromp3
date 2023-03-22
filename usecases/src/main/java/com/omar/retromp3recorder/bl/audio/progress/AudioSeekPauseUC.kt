package com.omar.retromp3recorder.bl.audio.progress

import com.omar.retromp3recorder.audioplayer.AudioPlayer
import javax.inject.Inject

class AudioSeekPauseUC @Inject constructor(
    private val audioPlayer: AudioPlayer
) {
    fun execute() {
        if (audioPlayer.observeState().blockingFirst() == AudioPlayer.State.Playing) {
            audioPlayer.onInput(AudioPlayer.Input.SeekPause)
        }
    }
}
