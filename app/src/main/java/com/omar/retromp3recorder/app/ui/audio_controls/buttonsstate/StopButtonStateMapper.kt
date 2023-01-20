package com.omar.retromp3recorder.app.ui.audio_controls.buttonsstate

import com.omar.retromp3recorder.app.ui.audio_controls.compose.InteractiveButtonState
import com.omar.retromp3recorder.bl.audio.AudioState
import com.omar.retromp3recorder.bl.audio.AudioStateMapper
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class StopButtonStateMapper @Inject constructor(
    private val audioStateMapper: AudioStateMapper,
) {
    fun observe(): Observable<InteractiveButtonState> =
        audioStateMapper.observe().map { audioState ->
            when (audioState) {
                is AudioState.Recording -> InteractiveButtonState.ENABLED
                is AudioState.Playing -> InteractiveButtonState.ENABLED
                is AudioState.Idle -> InteractiveButtonState.DISABLED
                is AudioState.Seek_Paused -> InteractiveButtonState.ENABLED
            }
        }
}
