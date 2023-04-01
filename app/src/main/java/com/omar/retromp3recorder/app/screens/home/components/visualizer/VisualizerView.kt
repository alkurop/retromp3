package com.omar.retromp3recorder.app.screens.home.components.visualizer

import androidx.compose.runtime.Immutable
import com.omar.retromp3recorder.bl.audio.progress.AudioState

object VisualizerView {
    @Immutable
    data class State(
        val audioState: AudioState = AudioState.Idle,
        val playerId: Int? = null
    )

    sealed class Input

    sealed class Output {
        data class PlayerIdOutput(val playerId: Int) : Output()
        data class AudioStateChanged(val state: AudioState) : Output()
    }
}
