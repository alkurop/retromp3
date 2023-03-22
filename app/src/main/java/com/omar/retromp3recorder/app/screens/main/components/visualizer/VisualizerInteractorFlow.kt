package com.omar.retromp3recorder.app.screens.main.components.visualizer

import com.omar.retromp3recorder.app.Interactor
import com.omar.retromp3recorder.bl.audio.progress.AudioStateMapper
import com.omar.retromp3recorder.bl.audio.PlayerIdMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.rx3.asFlow
import javax.inject.Inject

class VisualizerInteractorFlow @Inject constructor(
    private val playerIdMapper: PlayerIdMapper,
    private val audioStateMapper: AudioStateMapper,
    dispatcher: CoroutineDispatcher,
) : Interactor<Unit, VisualizerView.Output>(dispatcher) {

    override fun listRepos(): List<Flow<VisualizerView.Output>> {
        return listOf(audioStateMapper.observe().asFlow()
            .map { state -> VisualizerView.Output.AudioStateChanged(state) },
            playerIdMapper.flow()
                .map { playerId -> VisualizerView.Output.PlayerIdOutput(playerId) })
    }

    override suspend fun FlowCollector<VisualizerView.Output>.launchUseCase(input: Unit) {
        // noop
    }
}
