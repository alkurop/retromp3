package com.omar.retromp3recorder.app.ui.audio_controls

import com.omar.retromp3recorder.app.ui.audio_controls.compose.InteractiveButtonState
import com.omar.retromp3recorder.dto.FromToMillis

object AudioControlsView {
    sealed class Input {
        object Play : Input()
        object Record : Input()
        object Share : Input()
        object Stop : Input()
    }

    sealed class Output {
        data class PlayButtonState(val state: InteractiveButtonState) : Output()
        data class RecordButtonState(val state: InteractiveButtonState) : Output()
        data class ShareButtonState(val state: InteractiveButtonState) : Output()
        data class StopButtonState(val state: InteractiveButtonState) : Output()
        data class PlayerProgressState(val state: ProgressDisplay?) : Output()
        data class RecorderDurationState(val duration: Long?) : Output()
    }

    data class State(
        val playButtonState: InteractiveButtonState = InteractiveButtonState.DISABLED,
        val playerProgressState: ProgressDisplay? = null,
        val recordingDuration: Long? = null,
        val recordButtonState: InteractiveButtonState = InteractiveButtonState.DISABLED,
        val stopButtonState: InteractiveButtonState = InteractiveButtonState.DISABLED,
        val shareButtonState: InteractiveButtonState = InteractiveButtonState.DISABLED,
    )

    data class ProgressDisplay(
        val data: FromToMillis
    )
}