package com.omar.retromp3recorder.bl.audio.progress

import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class AudioStateMapper @Inject constructor(
    private val player: AudioPlayer,
    private val recorder: Mp3VoiceRecorder
) {
    fun flow(): Flow<AudioState> = combine(
        player.stateFlow(),
        recorder.stateFlow()
    ) { playerState, recorderState ->
        when {
            playerState == AudioPlayer.State.Playing -> AudioState.Playing
            playerState == AudioPlayer.State.PausedToSeek -> AudioState.SeekPaused
            recorderState == Mp3VoiceRecorder.State.Recording -> AudioState.Recording
            else -> AudioState.Idle
        }
    }.distinctUntilChanged()
}

sealed class AudioState {
    object Idle : AudioState()
    object Playing : AudioState()
    // state when user is holding finger on seek bar
    object SeekPaused : AudioState()
    object Recording : AudioState()
}
