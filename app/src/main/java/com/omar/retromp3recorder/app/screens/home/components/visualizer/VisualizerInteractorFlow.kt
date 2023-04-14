package com.omar.retromp3recorder.app.screens.home.components.visualizer

import com.omar.retromp3recorder.app.Interactor
import com.omar.retromp3recorder.bl.audio.record.PlayerIdMapper
import com.omar.retromp3recorder.bl.audio.progress.AudioStateMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class VisualizerInteractorFlow @Inject constructor(
    private val playerIdMapper: PlayerIdMapper,
    private val audioStateMapper: AudioStateMapper,
    dispatcher: CoroutineDispatcher,
) : Interactor<Unit, VisualizerView.Output>(dispatcher) {

    override fun listRepos(): List<Flow<VisualizerView.Output>> {
        return listOf(audioStateMapper.flow()
            .map { state -> VisualizerView.Output.AudioStateChanged(state) },
            playerIdMapper.flow()
                .map { playerId -> VisualizerView.Output.PlayerIdOutput(playerId) })
    }

    override suspend fun ProducerScope<VisualizerView.Output>.launchUseCase(input: Unit) {
        // noop
    }
}
