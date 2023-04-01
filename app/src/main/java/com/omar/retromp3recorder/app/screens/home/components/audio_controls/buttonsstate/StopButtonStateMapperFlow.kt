package com.omar.retromp3recorder.app.screens.home.components.audio_controls.buttonsstate

import com.omar.retromp3recorder.bl.audio.progress.AudioState
import com.omar.retromp3recorder.bl.audio.progress.AudioStateMapper
import com.omar.retromp3recorder.ui.statebutton.InteractiveButtonState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StopButtonStateMapperFlow @Inject constructor(
    private val audioStateMapper: AudioStateMapper,
) {
    fun flow(): Flow<InteractiveButtonState> =
        audioStateMapper.flow().map { audioState ->
            when (audioState) {
                is AudioState.Recording -> InteractiveButtonState.ENABLED
                is AudioState.Playing -> InteractiveButtonState.ENABLED
                is AudioState.Idle -> InteractiveButtonState.DISABLED
                is AudioState.SeekPaused -> InteractiveButtonState.ENABLED
            }
        }
}
