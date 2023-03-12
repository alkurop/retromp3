package com.omar.retromp3recorder.app.screens.main.components.audio_controls

import com.omar.retromp3recorder.bl.audio.JoinedProgressMapper
import com.omar.retromp3recorder.domain.JoinedProgress
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class RecorderDurationStateMapper @Inject constructor(
    private val joinedProgressRepo: JoinedProgressMapper,
) {
    fun observe(): Observable<AudioControlsView.Output.RecorderDurationState> =
        joinedProgressRepo.observe().map { joinedProgressState ->
            when (joinedProgressState) {
                JoinedProgress.Hidden,
                JoinedProgress.Intermediate,
                is JoinedProgress.PlayerProgressShown -> AudioControlsView.Output.RecorderDurationState(
                    null
                )
                is JoinedProgress.RecorderProgressShown -> AudioControlsView.Output.RecorderDurationState(
                    joinedProgressState.progress
                )
            }
        }
}
