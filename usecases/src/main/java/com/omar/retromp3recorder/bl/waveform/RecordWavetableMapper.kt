package com.omar.retromp3recorder.bl.waveform

import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.utils.platform.chunked
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import kotlin.math.absoluteValue

class RecordWavetableMapper @Inject constructor(
    private val recorder: Mp3VoiceRecorder,
) {
    fun flow(): Flow<Byte> {
        return recorder.recorderFlow()
            .chunked(10, 50)
            .map { array ->
                val r = array
                    .map { it.toList() }.flatten()
                    .map { item -> item.toInt().absoluteValue }
                    .max()
                    .toInt()
                    .toByte()
                r
            }
            .combine(recorder.stateFlow())
            { state, byte -> state to byte }
            .takeWhile { it.second != Mp3VoiceRecorder.State.Idle }
            .map { it.first }
    }
}
