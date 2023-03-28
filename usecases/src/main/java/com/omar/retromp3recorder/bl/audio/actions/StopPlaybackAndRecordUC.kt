package com.omar.retromp3recorder.bl.audio.actions

import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.bl.audio.progress.AudioState
import com.omar.retromp3recorder.bl.audio.progress.AudioStateMapper
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class StopPlaybackAndRecordUC @Inject constructor(
    private val audioPlayer: AudioPlayer,
    private val stopRecordUC: StopRecordUCSuspend,
    private val stateMapper: AudioStateMapper,
) {
    suspend fun execute() {
        when (stateMapper.flow().first()) {
            AudioState.Playing,
            AudioState.SeekPaused -> {
                audioPlayer.onInput(
                    AudioPlayer.Input.Stop
                )
            }
            AudioState.Recording -> stopRecordUC.execute()
            is AudioState.Idle -> {}
        }
    }
}
