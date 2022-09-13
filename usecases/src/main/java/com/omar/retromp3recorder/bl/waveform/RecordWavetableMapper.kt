package com.omar.retromp3recorder.bl.waveform

import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.absoluteValue

class RecordWavetableMapper @Inject constructor(
    private val recorder: Mp3VoiceRecorder,
) {
    fun observe(): Observable<Byte> {
        return recorder.observeRecorder()
            .map { array ->
                array.toList().map { it.toInt().absoluteValue }.average()
            }
            .buffer(Mp3VoiceRecorder.WaveTableSampleRate._100.value.toLong(), TimeUnit.MILLISECONDS)
            .map { it.average() }
            .map { it.toInt().toByte() }

    }
}
