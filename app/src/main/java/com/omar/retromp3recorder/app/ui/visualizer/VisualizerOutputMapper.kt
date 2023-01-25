package com.omar.retromp3recorder.app.ui.visualizer

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.functions.BiFunction

object VisualizerOutputMapper {
    internal fun mapOutputToState(): ObservableTransformer<VisualizerView.Output, VisualizerView.State> =
        ObservableTransformer { upstream: Observable<VisualizerView.Output> ->
            upstream.scan(defaultViewModel, mapper)
        }

    private val mapper: BiFunction<VisualizerView.State, VisualizerView.Output, VisualizerView.State> =
        BiFunction { oldState: VisualizerView.State, result: VisualizerView.Output ->
            when (result) {
                is VisualizerView.Output.AudioStateChanged -> oldState.copy(
                    audioState = result.state
                )
                is VisualizerView.Output.PlayerIdOutput -> oldState.copy(
                    playerId = result.playerId
                )
            }
        }
    private val defaultViewModel = VisualizerView.State()
}