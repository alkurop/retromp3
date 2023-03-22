package com.omar.retromp3recorder.app.screens.main.components.audio_controls.buttonsstate

import com.omar.retromp3recorder.app.screens.main.components.audio_controls.compose.InteractiveButtonState
import com.omar.retromp3recorder.bl.audio.progress.AudioState
import com.omar.retromp3recorder.bl.audio.progress.AudioStateMapperFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RecordButtonStateFlow @Inject constructor(
    private val audioStateMapper: AudioStateMapperFlow,
) {
    fun flow(): Flow<InteractiveButtonState> =
        audioStateMapper.flow().map { audioState ->
            when (audioState) {
                is AudioState.Recording -> InteractiveButtonState.RUNNING
                is AudioState.Playing -> InteractiveButtonState.DISABLED
                is AudioState.Seek_Paused -> InteractiveButtonState.DISABLED
                is AudioState.Idle -> InteractiveButtonState.ENABLED
            }
        }
}
