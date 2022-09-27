package com.omar.retromp3recorder.app.ui.audio_controls

import com.omar.retromp3recorder.dto.FromToMillis
import com.omar.retromp3recorder.ui.state_button.InteractiveButton

object AudioControlsView {
    sealed class Input {
        object Play : Input()
        object Record : Input()
        object Share : Input()
        object Stop : Input()
    }

    sealed class Output {
        data class PlayButtonState(val state: InteractiveButton.State) : Output()
        data class RecordButtonState(val state: InteractiveButton.State) : Output()
        data class ShareButtonState(val state: InteractiveButton.State) : Output()
        data class StopButtonState(val state: InteractiveButton.State) : Output()
        data class PlayerProgressState(val state: ProgressDisplay?) : Output()
        data class RecorderDurationState(val duration: Long?) : Output()
    }

    data class State(
        val playButtonState: InteractiveButton.State = InteractiveButton.State.DISABLED,
        val playerProgressState: ProgressDisplay? = null,
        val recordingDuration: Long? = null,
        val recordButtonState: InteractiveButton.State = InteractiveButton.State.DISABLED,
        val stopButtonState: InteractiveButton.State = InteractiveButton.State.DISABLED,
        val shareButtonState: InteractiveButton.State = InteractiveButton.State.DISABLED,
    )

    data class ProgressDisplay(
        val data: FromToMillis
    )
}