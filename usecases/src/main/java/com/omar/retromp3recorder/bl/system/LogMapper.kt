package com.omar.retromp3recorder.bl.system

import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.audioplayer.observeEvents
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.share.Sharer
import com.omar.retromp3recorder.storage.repo.global.LogsRepo
import com.omar.retromp3recorder.domain.platform.LogEvent
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class LogMapper @Inject constructor(
    private val recorder: Mp3VoiceRecorder,
    private val sharer: Sharer,
    private val logsRepo: LogsRepo
) {

    fun observe(): Observable<LogEvent> = Observable.merge(
        recorder.createLogs(),
        sharer.createLogs(),
        logsRepo.observe()
    )
}

private fun Mp3VoiceRecorder.createLogs(): Observable<LogEvent> {
    val message = this
        .observeEvents()
        .ofType(Mp3VoiceRecorder.Event.Message::class.java)
        .map { answer -> LogEvent.Message(answer.message) }
    val error = this
        .observeEvents()
        .ofType(Mp3VoiceRecorder.Event.Error::class.java)
        .map { answer -> LogEvent.Error(answer.error) }
    return Observable.merge(message, error)
}

@Suppress("UNUSED")
private fun AudioPlayer.createLogs(): Observable<LogEvent> {
    val message: Observable<LogEvent> = this
        .observeEvents()
        .ofType(AudioPlayer.Output.Event.Message::class.java)
        .map { answer -> LogEvent.Message(answer.message) }
    val error: Observable<LogEvent> = this
        .observeEvents()
        .ofType(AudioPlayer.Output.Event.Error::class.java)
        .map { answer -> LogEvent.Error(answer.error) }

    return Observable.merge(message, error)
}

private fun Sharer.createLogs(): Observable<LogEvent> {
    val message = this
        .observeEvents()
        .ofType(Sharer.Event.SharingOk::class.java)
        .map { answer -> LogEvent.Message(answer.message) }
    val error = this
        .observeEvents()
        .ofType(Sharer.Event.Error::class.java)
        .map { answer -> LogEvent.Error(answer.error) }
    return Observable.merge(message, error)
}
