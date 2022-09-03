package com.omar.retromp3recorder.bl.audio

import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import io.reactivex.rxjava3.core.Observable
import timber.log.Timber
import javax.inject.Inject

interface AudioStateMapper {
    fun observe(): Observable<AudioState>
}

class AudioStateMapperImpl @Inject constructor(
    private val player: AudioPlayer,
    private val recorder: Mp3VoiceRecorder
) : AudioStateMapper {
    override fun observe(): Observable<AudioState> = Observable
        .combineLatest(
            player.observeState(),
            recorder.observeState()
        ) { playerState, recorderState ->
            when {
                playerState == AudioPlayer.State.Playing -> AudioState.Playing
                playerState == AudioPlayer.State.Seek_Paused -> AudioState.Seek_Paused
                recorderState == Mp3VoiceRecorder.State.Recording -> AudioState.Recording
                else -> AudioState.Idle
            }
        }
        .distinctUntilChanged()
}

sealed class AudioState {
    object Idle : AudioState()
    object Playing : AudioState()
    object Seek_Paused : AudioState()
    object Recording : AudioState()
}