package com.omar.retromp3recorder.app.screens.main.components.visualizer

import com.omar.retromp3recorder.bl.audio.AudioStateMapper
import com.omar.retromp3recorder.bl.audio.PlayerIdMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.rx3.asFlow
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class VisualizerInteractorFlow @Inject constructor(
    private val playerIdMapper: PlayerIdMapper,
    private val audioStateMapper: AudioStateMapper,
    dispatcher: CoroutineDispatcher,
) : CoroutineScope {
    override val coroutineContext: CoroutineContext = dispatcher + Job()

    fun processIO(): Flow<VisualizerView.Output> {
        return listOf(
            audioStateMapper.observe().asFlow()
                .map { state -> VisualizerView.Output.AudioStateChanged(state) },
            playerIdMapper.flow()
                .map { playerId -> VisualizerView.Output.PlayerIdOutput(playerId) }
        ).merge()
    }
}
