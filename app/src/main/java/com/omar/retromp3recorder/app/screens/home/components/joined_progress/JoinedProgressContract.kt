package com.omar.retromp3recorder.app.screens.home.components.joined_progress

import androidx.compose.runtime.Immutable
import com.omar.retromp3recorder.domain.FileWrapper
import com.omar.retromp3recorder.domain.JoinedProgress
import com.omar.retromp3recorder.ui.wavetable.compose.WaveSeekData
import com.omar.retromp3recorder.ui.wavetable.compose.WavetableComposeData

object JoinedProgressContract {
    sealed class In {
        data class SeekToPosition(val position: Long) : In()
        object SeekingStarted : In()
        object SeekingFinished : In()
    }

    sealed class Output {
        data class JoinedProgressChanged(val joinedProgress: JoinedProgress) : Output()
        data class CurrentFileChanged(val currentFile: FileWrapper?) : Output()
    }

    @Immutable
    data class State(
        val seekState: SeekViewState = SeekViewState.Intermediate,
        val fileName: String? = null
    )


    sealed class SeekViewState {
        @Immutable
        data class Recorder(val data: WavetableComposeData) : SeekViewState()

        @Immutable
        data class Player(val data: WaveSeekData) : SeekViewState()
        object Hidden : SeekViewState()
        object Intermediate : SeekViewState()

    }
}
