package com.omar.retromp3recorder.app.ui.joined_progress

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.functions.BiFunction

object JoinedProgressViewMapper {
    fun mapOutputToState(): ObservableTransformer<JoinedProgressView.Output, JoinedProgressView.State> =
        ObservableTransformer { upstream: Observable<JoinedProgressView.Output> ->
            upstream.scan(
                JoinedProgressView.State(),
                getMapper()
            )
        }

    private fun getMapper(): BiFunction<JoinedProgressView.State, JoinedProgressView.Output, JoinedProgressView.State> =
        BiFunction { oldState: JoinedProgressView.State, output: JoinedProgressView.Output ->
            when (output) {
                is JoinedProgressView.Output.JoinedProgressChanged -> {
                    oldState.copy(
                        joinedProgress = output.joinedProgress
                    )
                }
                is JoinedProgressView.Output.CurrentFileChanged -> {
                    oldState.copy(currentFile = output.currentFile)
                }
            }
        }
}