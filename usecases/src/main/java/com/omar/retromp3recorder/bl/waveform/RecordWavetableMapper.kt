package com.omar.retromp3recorder.bl.waveform

import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.takeWhile
import javax.inject.Inject
import kotlin.math.absoluteValue

class RecordWavetableMapper @Inject constructor(
    private val recorder: Mp3VoiceRecorder,
) {
    fun flow(): Flow<Byte> {
        return recorder.recorderFlow()
            .map { array ->
                array.toList().map { it.toInt().absoluteValue }.average().toInt().toByte()
            }
            .combine(recorder.stateFlow())
            { state, byte -> state to byte }
            .takeWhile { it.second != Mp3VoiceRecorder.State.Idle }
            .map { it.first }
    }
}
