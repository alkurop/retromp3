package com.omar.retromp3recorder.app.ui.log

import com.omar.retromp3recorder.bl.system.LogMapper
import com.omar.retromp3recorder.domain.platform.LogEvent
import com.omar.retromp3recorder.utils.processIO
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

class LogInteractor @Inject constructor(
    private val scheduler: Scheduler,
    private val logMapper: LogMapper
) {
    fun processIO(): ObservableTransformer<LogView.Input, LogView.Output> =
        scheduler.processIO(
            inputMapper = { Completable.never() },
            outputMapper = mapRepoToOutput
        )

    private val mapRepoToOutput: () -> Observable<LogView.Output> = {
        Observable.merge(
            listOf(
                logMapper.observe()
                    .ofType(LogEvent.Message::class.java)
                    .map { message -> LogView.Output.MessageLogOutput(message.message) },
                logMapper.observe()
                    .ofType(LogEvent.Error::class.java)
                    .map { message -> LogView.Output.ErrorLogOutput(message.error) },
            )
        )
    }
}
