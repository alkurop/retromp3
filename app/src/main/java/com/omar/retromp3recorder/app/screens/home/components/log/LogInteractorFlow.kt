package com.omar.retromp3recorder.app.screens.home.components.log

import com.omar.retromp3recorder.app.Interactor
import com.omar.retromp3recorder.bl.system.LogMapper
import com.omar.retromp3recorder.domain.platform.LogEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LogInteractorFlow @Inject constructor(
    private val logMapper: LogMapper,
    dispatcher: CoroutineDispatcher
) : Interactor<LogView.Input, LogView.Output>(dispatcher) {

    override fun listRepos(): List<Flow<LogView.Output>> {
        return listOf(
            logMapper.flow()
                .filterIsInstance<LogEvent.Message>()
                .map { message -> LogView.Output.MessageLogOutput(message.message) },
            logMapper.flow()
                .filterIsInstance<LogEvent.Error>()
                .map { message -> LogView.Output.ErrorLogOutput(message.error) },
        )
    }

    override suspend fun FlowCollector<LogView.Output>.launchUseCase(input: LogView.Input) {
        //noop
    }
}
