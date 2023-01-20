package com.omar.retromp3recorder.app.ui.audio_controls.buttonsstate

import com.omar.retromp3recorder.app.ui.audio_controls.compose.InteractiveButtonState
import com.omar.retromp3recorder.bl.audio.AudioState
import com.omar.retromp3recorder.bl.audio.AudioStateMapper
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class RecordButtonStateMapper @Inject constructor(
    private val audioStateMapper: AudioStateMapper,
) {
    fun observe(): Observable<InteractiveButtonState> =
        audioStateMapper.observe().map { audioState ->
            when (audioState) {
                is AudioState.Recording -> InteractiveButtonState.RUNNING
                is AudioState.Playing -> InteractiveButtonState.DISABLED
                is AudioState.Seek_Paused -> InteractiveButtonState.DISABLED
                is AudioState.Idle -> InteractiveButtonState.ENABLED
            }
        }
}