package com.omar.retromp3recorder.bl.system

import com.github.alkurop.stringerbell.Stringer
import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.audioplayer.observeEvents
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.share.Sharer
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class LogMapper @Inject constructor(
    audioPlayer: AudioPlayer,
    recorder: Mp3VoiceRecorder,
    sharer: Sharer
) {
    private val observable: Observable<Event> = Observable.merge(
        audioPlayer.createLogs(),
        recorder.createLogs(),
        sharer.createLogs()
    ).share()

    fun observe(): Observable<Event> = observable

    sealed class Event {
        data class Message(val message: Stringer) : Event()
        data class Error(val error: Stringer) : Event()
    }
}

private fun Mp3VoiceRecorder.createLogs(): Observable<LogMapper.Event> {
    val message = this
        .observeEvents()
        .ofType(Mp3VoiceRecorder.Event.Message::class.java)
        .map { answer -> LogMapper.Event.Message(answer.message) }
    val error = this
        .observeEvents()
        .ofType(Mp3VoiceRecorder.Event.Error::class.java)
        .map { answer -> LogMapper.Event.Error(answer.error) }
    return Observable.merge(message, error)
}

private fun AudioPlayer.createLogs(): Observable<LogMapper.Event> {
    val message: Observable<LogMapper.Event> = this
        .observeEvents()
        .ofType(AudioPlayer.Output.Event.Message::class.java)
        .map { answer -> LogMapper.Event.Message(answer.message) }
    val error: Observable<LogMapper.Event> = this
        .observeEvents()
        .ofType(AudioPlayer.Output.Event.Error::class.java)
        .map { answer -> LogMapper.Event.Error(answer.error) }

    return Observable.merge(message, error)
}

private fun Sharer.createLogs(): Observable<LogMapper.Event> {
    val message = this
        .observeEvents()
        .ofType(Sharer.Event.SharingOk::class.java)
        .map { answer -> LogMapper.Event.Message(answer.message) }
    val error = this
        .observeEvents()
        .ofType(Sharer.Event.Error::class.java)
        .map { answer -> LogMapper.Event.Error(answer.error) }
    return Observable.merge(message, error)
}
