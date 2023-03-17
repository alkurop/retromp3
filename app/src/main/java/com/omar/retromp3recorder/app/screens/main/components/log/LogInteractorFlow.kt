package com.omar.retromp3recorder.app.screens.main.components.log

import com.omar.retromp3recorder.bl.system.LogMapperFlow
import com.omar.retromp3recorder.domain.platform.LogEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class LogInteractorFlow @Inject constructor(
    private val logMapper: LogMapperFlow,
    private val dispatcher: CoroutineDispatcher
) {
    fun processIO(): Flow<LogView.Output> =
        listOf(
            listenToRepos()
        ).merge().flowOn(dispatcher)

    private fun listenToRepos(): Flow<LogView.Output> {
        return listOf(
            logMapper.flow()
                .filterIsInstance<LogEvent.Message>()
                .map { message -> LogView.Output.MessageLogOutput(message.message) },
            logMapper.flow()
                .filterIsInstance<LogEvent.Error>()
                .map { message -> LogView.Output.ErrorLogOutput(message.error) },
        ).merge()
    }
}
