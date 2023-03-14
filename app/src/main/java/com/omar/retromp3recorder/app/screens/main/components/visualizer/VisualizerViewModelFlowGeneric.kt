package com.omar.retromp3recorder.app.screens.main.components.visualizer

import com.omar.retromp3recorder.app.FlowViewModel
import com.omar.retromp3recorder.app.screens.main.components.visualizer.VisualizerOutputMapper.mapOutputToStateFlow
import com.omar.retromp3recorder.bl.audio.AudioStateMapper
import com.omar.retromp3recorder.bl.audio.PlayerIdMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.rx3.asFlow
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class VisualizerViewModelFlowGeneric @Inject constructor(
    private val playerIdMapper: PlayerIdMapper,
    private val audioStateMapper: AudioStateMapper,
    dispatcher: CoroutineDispatcher
) : FlowViewModel<Unit, VisualizerView.Output, VisualizerView.State>(dispatcher) {

    override val coroutineContext: CoroutineContext =  dispatcher + SupervisorJob()

    override val defaultState: VisualizerView.State = VisualizerView.State()

    override fun listenToRepos(): Flow<VisualizerView.Output> {
        return listOf(
            audioStateMapper.observe().asFlow()
                .map { state -> VisualizerView.Output.AudioStateChanged(state) },
            playerIdMapper.flow()
                .map { playerId -> VisualizerView.Output.PlayerIdOutput(playerId) }
        ).merge()
    }

    override suspend fun FlowCollector<VisualizerView.Output>.getUsecase(event: Unit) {
        // noop
    }

    override fun Flow<VisualizerView.Output>.mapToState(): Flow<VisualizerView.State> {
        return this.mapOutputToStateFlow()
    }
}
