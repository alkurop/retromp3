package com.omar.retromp3recorder.app.screens.main.components.log

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.functions.BiFunction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.scan

object LogOutputMapper {

    fun Flow<LogView.Output>.mapOutputToState(): Flow<LogView.State> {
        return this.scan(LogView.State()) { oldState, output ->
            oldState.copy(
                messages = oldState.messages.takeLast(LOG_MEMORY_SIZE) + output,
            )
        }
    }

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
