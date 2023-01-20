package com.omar.retromp3recorder.app.ui.audio_controls.buttonsstate

import com.omar.retromp3recorder.app.ui.audio_controls.compose.InteractiveButtonState
import com.omar.retromp3recorder.bl.audio.AudioState
import com.omar.retromp3recorder.bl.audio.AudioStateMapper
import com.omar.retromp3recorder.bl.files.HasPlayableFileMapper
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class PlayButtonStateMapper @Inject constructor(
    private val audioStateMapper: AudioStateMapper,
    private val hasPlayableFileMapper: HasPlayableFileMapper
) {
    fun observe(): Observable<InteractiveButtonState> =
        Observable.combineLatest(
            hasPlayableFileMapper.observe(),
            audioStateMapper.observe()
        ) { hasFile, audioState ->
            when (audioState) {
                is AudioState.Recording -> InteractiveButtonState.DISABLED
                is AudioState.Seek_Paused -> InteractiveButtonState.ENABLED
                is AudioState.Playing -> InteractiveButtonState.RUNNING
                is AudioState.Idle -> if (hasFile.hasValue()) InteractiveButtonState.ENABLED else InteractiveButtonState.DISABLED
            }
        }
}
