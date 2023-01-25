package com.omar.retromp3recorder.app.ui.visualizer

import com.omar.retromp3recorder.bl.audio.AudioState

object VisualizerView {
    data class State(
        val audioState: AudioState = AudioState.Idle,
        val playerId: Int? = null
    )

    sealed class Input

    sealed class Output {
        data class PlayerIdOutput(val playerId: Int) : VisualizerView.Output()
        data class AudioStateChanged(val state: AudioState) : VisualizerView.Output()
    }
}