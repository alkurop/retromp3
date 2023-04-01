package com.omar.retromp3recorder.app.screens.home.components.visualizer

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.scan

object VisualizerOutputMapper {
    fun Flow<VisualizerView.Output>.mapOutputToStateFlow(): Flow<VisualizerView.State> {
        return this.scan(VisualizerView.State()){ oldState, result ->
            when (result) {
                is VisualizerView.Output.AudioStateChanged -> oldState.copy(
                    audioState = result.state
                )
                is VisualizerView.Output.PlayerIdOutput -> oldState.copy(
                    playerId = result.playerId
                )
            }
        }
    }
}
