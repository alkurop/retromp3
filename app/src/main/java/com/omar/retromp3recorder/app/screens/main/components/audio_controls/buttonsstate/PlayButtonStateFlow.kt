package com.omar.retromp3recorder.app.screens.main.components.audio_controls.buttonsstate

import com.omar.retromp3recorder.app.screens.main.components.audio_controls.compose.InteractiveButtonState
import com.omar.retromp3recorder.bl.audio.progress.AudioState
import com.omar.retromp3recorder.bl.audio.progress.AudioStateMapper
import com.omar.retromp3recorder.bl.files.HasPlayableFileMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.rx3.asFlow
import javax.inject.Inject

class PlayButtonStateFlow @Inject constructor(
    private val audioStateMapper: AudioStateMapper,
    private val hasPlayableFileMapper: HasPlayableFileMapper
) {
    fun flow(): Flow<InteractiveButtonState> =
        combine(
            hasPlayableFileMapper.observe().asFlow(),
            audioStateMapper.observe().asFlow()
        ) { hasFile, audioState ->
            when (audioState) {
                is AudioState.Recording -> InteractiveButtonState.DISABLED
                is AudioState.Seek_Paused -> InteractiveButtonState.ENABLED
                is AudioState.Playing -> InteractiveButtonState.RUNNING
                is AudioState.Idle -> if (hasFile.hasValue()) InteractiveButtonState.ENABLED else InteractiveButtonState.DISABLED
            }
        }
}
