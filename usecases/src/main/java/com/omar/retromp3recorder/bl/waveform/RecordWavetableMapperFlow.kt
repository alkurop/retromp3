package com.omar.retromp3recorder.bl.waveform

import com.omar.retromp3recorder.bl.audio.progress.AudioState
import com.omar.retromp3recorder.bl.audio.progress.AudioStateMapper
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.absoluteValue

class RecordWavetableMapperFlow @Inject constructor(
    private val recorder: Mp3VoiceRecorder,
    private val audioStateMapper: AudioStateMapper
) {
    fun flow(): Flow<Byte> {
        return recorder.recorderFlow()
            .map { array ->
                array.toList().map { it.toInt().absoluteValue }.average()
            }
            .chunked(10_000, 100)
            .map { it.average() }
            .map { it.toInt().toByte() }
            .combine(audioStateMapper.flow())
            { state, byte -> state to byte }
            .takeWhile { it.second !is AudioState.Idle }
            .map { it.first }
    }
}

fun <T> Flow<T>.chunked(maxSize: Int, intervalMillis: Long) = channelFlow {

    val buffer = mutableListOf<T>()
    var flushJob: Job? = null

    collect { value ->

        flushJob?.cancelAndJoin()
        buffer.add(value)

        if (buffer.size >= maxSize) {
            send(buffer.toList())
            buffer.clear()
        } else {
            flushJob = launch {
                delay(intervalMillis)
                if (buffer.isNotEmpty()) {
                    send(buffer.toList())
                    buffer.clear()
                }
            }
        }
    }

    flushJob?.cancelAndJoin()

    if (buffer.isNotEmpty()) {
        send(buffer.toList())
        buffer.clear()
    }
}
