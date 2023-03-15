package com.omar.retromp3recorder.app.screens.main.components.audio_controls

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.scan

object AudioControlsOutputMapper {

    fun Flow<AudioControlsView.Output>.mapOutputToStateFlow(): Flow<AudioControlsView.State> {
        return this.scan(AudioControlsView.State()) { oldState, output ->
            when (output) {
                is AudioControlsView.Output.PlayButtonState -> oldState.copy(playButtonState = output.state)
                is AudioControlsView.Output.RecordButtonState -> oldState.copy(recordButtonState = output.state)
                is AudioControlsView.Output.ShareButtonState -> oldState.copy(shareButtonState = output.state)
                is AudioControlsView.Output.StopButtonState -> oldState.copy(stopButtonState = output.state)
                is AudioControlsView.Output.PlayerProgressState -> oldState.copy(playerProgressState = output.state?.toProgressDisplay())
                is AudioControlsView.Output.RecorderDurationState -> oldState.copy(recordingDuration = output.duration)
            }
        }
    }
}
