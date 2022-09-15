package com.omar.retromp3recorder.app.ui.audio_controls.buttonsstate

import com.omar.retromp3recorder.bl.audio.AudioState
import com.omar.retromp3recorder.bl.audio.AudioStateMapper
import com.omar.retromp3recorder.bl.files.HasPlayableFileMapper
import com.omar.retromp3recorder.ui.state_button.InteractiveButton
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class PlayButtonStateMapper @Inject constructor(
    private val audioStateMapper: AudioStateMapper,
    private val hasPlayableFileMapper: HasPlayableFileMapper
) {
    fun observe(): Observable<InteractiveButton.State> =
        Observable.combineLatest(
            hasPlayableFileMapper.observe(),
            audioStateMapper.observe()
        ) { hasFile, audioState ->
            when (audioState) {
                is AudioState.Recording -> InteractiveButton.State.DISABLED
                is AudioState.Seek_Paused -> InteractiveButton.State.ENABLED
                is AudioState.Playing -> InteractiveButton.State.RUNNING
                is AudioState.Idle -> if (hasFile.hasValue()) InteractiveButton.State.ENABLED else InteractiveButton.State.DISABLED
            }
        }
}
