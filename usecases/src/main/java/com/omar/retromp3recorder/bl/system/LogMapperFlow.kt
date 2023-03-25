package com.omar.retromp3recorder.bl.system

import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.audioplayer.eventsFlow
import com.omar.retromp3recorder.domain.platform.LogEvent
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.share.Sharer
import com.omar.retromp3recorder.storage.repo.global.LogRepo
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.rx3.asFlow
import javax.inject.Inject

class LogMapperFlow @Inject constructor(
    private val recorder: Mp3VoiceRecorder,
    private val sharer: Sharer,
    private val logRepo: LogRepo,
) {

    fun flow(): Flow<LogEvent> = merge(
            recorder.createLogs(),
            sharer.createLogs(),
            logRepo.flow()
        )

    private companion object {
        private fun Mp3VoiceRecorder.createLogs(): Flow<LogEvent> {
            val message = this
                .observeEvents()
                .asFlow()
                .filterIsInstance<Mp3VoiceRecorder.Event.Message>()
                .map { answer -> LogEvent.Message(answer.message) }
            val error = this
                .observeEvents()
                .asFlow()
                .filterIsInstance<Mp3VoiceRecorder.Event.Error>()
                .map { answer -> LogEvent.Error(answer.error) }
            return merge(message, error)
        }

        @Suppress("UNUSED")
        fun AudioPlayer.createLogs(): Flow<LogEvent> {
            val message: Flow<LogEvent> = this
                .eventsFlow()
                .filterIsInstance<AudioPlayer.Output.Event.Message>()
                .map { answer -> LogEvent.Message(answer.message) }
            val error: Flow<LogEvent> = this
                .eventsFlow()
                .filterIsInstance<AudioPlayer.Output.Event.Error>()
                .map { answer -> LogEvent.Error(answer.error) }

            return merge(message, error)
        }

        fun Sharer.createLogs(): Flow<LogEvent> {
            val message = this
                .flow()
                .filterIsInstance<Sharer.Event.SharingOk>()
                .map { answer -> LogEvent.Message(answer.message) }
            val error = this
                .flow()
                .filterIsInstance<Sharer.Event.Error>()
                .map { answer -> LogEvent.Error(answer.error) }
            return merge(message, error)
        }
    }
}
