package com.omar.retromp3recorder.app.screens.main.components.audio_controls.buttonsstate

import com.omar.retromp3recorder.app.screens.main.components.audio_controls.compose.InteractiveButtonState
import com.omar.retromp3recorder.bl.audio.progress.AudioState
import com.omar.retromp3recorder.bl.audio.progress.AudioStateMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.rx3.asFlow
import javax.inject.Inject

class StopButtonStateMapperFlow @Inject constructor(
    private val audioStateMapper: AudioStateMapper,
) {
    fun flow(): Flow<InteractiveButtonState> =
        audioStateMapper.observe().asFlow().map { audioState ->
            when (audioState) {
                is AudioState.Recording -> InteractiveButtonState.ENABLED
                is AudioState.Playing -> InteractiveButtonState.ENABLED
                is AudioState.Idle -> InteractiveButtonState.DISABLED
                is AudioState.Seek_Paused -> InteractiveButtonState.ENABLED
            }
        }
}
