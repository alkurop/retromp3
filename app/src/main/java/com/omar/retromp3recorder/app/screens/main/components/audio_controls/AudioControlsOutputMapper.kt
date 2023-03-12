package com.omar.retromp3recorder.app.screens.main.components.audio_controls

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.functions.BiFunction

object AudioControlsOutputMapper {
    internal fun mapOutputToState(): ObservableTransformer<AudioControlsView.Output, AudioControlsView.State> =
        ObservableTransformer { upstream: Observable<AudioControlsView.Output> ->
            upstream.scan(AudioControlsView.State(), mapper)
        }

    private val mapper: BiFunction<AudioControlsView.State, AudioControlsView.Output, AudioControlsView.State> =
        BiFunction { oldState: AudioControlsView.State, output: AudioControlsView.Output ->
            when (output) {
                is AudioControlsView.Output.PlayButtonState -> oldState.copy(playButtonState = output.state)
                is AudioControlsView.Output.RecordButtonState -> oldState.copy(recordButtonState = output.state)
                is AudioControlsView.Output.ShareButtonState -> oldState.copy(shareButtonState = output.state)
                is AudioControlsView.Output.StopButtonState -> oldState.copy(stopButtonState = output.state)
                is AudioControlsView.Output.PlayerProgressState -> oldState.copy(playerProgressState = output.state)
                is AudioControlsView.Output.RecorderDurationState -> oldState.copy(recordingDuration = output.duration)
            }
        }
}
