package com.omar.retromp3recorder.app.screens.main.components.audio_controls

import com.omar.retromp3recorder.bl.audio.JoinedProgressMapper
import com.omar.retromp3recorder.domain.JoinedProgress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.rx3.asFlow
import javax.inject.Inject

class RecorderDurationStateFlow @Inject constructor(
    private val joinedProgressRepo: JoinedProgressMapper,
) {
    fun flow(): Flow<AudioControlsView.Output.RecorderDurationState> =
        joinedProgressRepo.observe().asFlow().map { joinedProgressState ->
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
