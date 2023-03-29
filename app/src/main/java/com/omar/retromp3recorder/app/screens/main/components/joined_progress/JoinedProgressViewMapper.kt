package com.omar.retromp3recorder.app.screens.main.components.joined_progress

import com.omar.retromp3recorder.app.utils.toFileName
import com.omar.retromp3recorder.domain.JoinedProgress
import com.omar.retromp3recorder.ui.wavetable.compose.WaveSeekData
import com.omar.retromp3recorder.ui.wavetable.compose.WavetableComposeData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.scan

object JoinedProgressViewMapper {
    fun Flow<JoinedProgressContract.Output>.mapOutputToStateFlow(): Flow<JoinedProgressContract.State> {
        return this.scan(JoinedProgressContract.State()) { oldState, output ->
            when (output) {
                is JoinedProgressContract.Output.JoinedProgressChanged -> {
                    oldState.copy(
                        seekState = output.joinedProgress.mapToViewState()
                    )
                }
                is JoinedProgressContract.Output.CurrentFileChanged -> {
                    oldState.copy(
                        fileName = output.currentFile?.path?.toFileName()
                    )
                }
            }
        }
    }


    private fun JoinedProgress.mapToViewState(): JoinedProgressContract.SeekViewState {
        return when (this) {
            is JoinedProgress.Hidden -> JoinedProgressContract.SeekViewState.Hidden
            is JoinedProgress.Intermediate -> JoinedProgressContract.SeekViewState.Intermediate
            is JoinedProgress.RecorderProgressShown -> JoinedProgressContract.SeekViewState.Recorder(
                WavetableComposeData(this.wavetable)
            )
            is JoinedProgress.PlayerProgressShown -> {
                JoinedProgressContract.SeekViewState.Player(
                    WaveSeekData(this.progress, this.wavetable)
                )
            }
        }
    }
}
