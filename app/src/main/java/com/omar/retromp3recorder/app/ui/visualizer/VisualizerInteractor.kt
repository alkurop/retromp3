package com.omar.retromp3recorder.app.ui.visualizer

import com.omar.retromp3recorder.bl.audio.AudioStateMapper
import com.omar.retromp3recorder.bl.audio.PlayerIdMapper
import com.omar.retromp3recorder.utils.domain.processIO
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

class VisualizerInteractor @Inject constructor(
    private val scheduler: Scheduler,
    private val playerIdMapper: PlayerIdMapper,
    private val audioStateMapper: AudioStateMapper
) {
    fun processIO(): ObservableTransformer<VisualizerView.Input, VisualizerView.Output> =
        scheduler.processIO(
            inputMapper = { Completable.never() },
            outputMapper = mapRepoToOutput
        )

    private val mapRepoToOutput: () -> Observable<VisualizerView.Output> = {
        Observable.merge(listOf(
            audioStateMapper.observe()
                .map { state -> VisualizerView.Output.AudioStateChanged(state) },
            playerIdMapper.observe()
                .map { playerId -> VisualizerView.Output.PlayerIdOutput(playerId) }
        ))
    }
}
