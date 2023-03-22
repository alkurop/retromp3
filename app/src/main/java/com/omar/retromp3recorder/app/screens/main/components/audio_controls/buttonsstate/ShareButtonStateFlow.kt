package com.omar.retromp3recorder.app.screens.main.components.audio_controls.buttonsstate

import com.omar.retromp3recorder.app.screens.main.components.audio_controls.compose.InteractiveButtonState
import com.omar.retromp3recorder.bl.audio.progress.AudioState
import com.omar.retromp3recorder.bl.audio.progress.AudioStateMapper
import com.omar.retromp3recorder.bl.files.HasPlayableFileMapperFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class ShareButtonStateFlow @Inject constructor(
    private val audioStateMapper: AudioStateMapper,
    private val hasPlayableFileMapper: HasPlayableFileMapperFlow
) {
    fun flow(): Flow<InteractiveButtonState> =
        combine(
            hasPlayableFileMapper.flow(),
            audioStateMapper.flow()
        )
        { hasFile, audioState ->
            when (audioState) {
                is AudioState.Recording -> InteractiveButtonState.DISABLED
                is AudioState.Playing -> InteractiveButtonState.ENABLED
                is AudioState.Seek_Paused -> InteractiveButtonState.DISABLED
                is AudioState.Idle -> if (hasFile.hasValue()) InteractiveButtonState.ENABLED else InteractiveButtonState.DISABLED
            }
        }
}
