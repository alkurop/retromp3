package com.omar.retromp3recorder.app.ui.log

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.functions.BiFunction

object LogOutputMapper {
    fun mapOutputToState(): ObservableTransformer<LogView.Output, LogView.State> =
        ObservableTransformer { upstream: Observable<LogView.Output> ->
            upstream.scan(
                getDefaultViewModel(),
                getMapper()
            )
        }

    private fun getMapper(): BiFunction<LogView.State, LogView.Output, LogView.State> =
        BiFunction { oldState: LogView.State, output: LogView.Output ->
            oldState.copy(
                messages = oldState.messages.takeLast(LOG_MEMORY_SIZE) + output,
            )
        }

    private fun getDefaultViewModel() = LogView.State(
        messages = emptyList()
    )
}

private const val LOG_MEMORY_SIZE = 30
