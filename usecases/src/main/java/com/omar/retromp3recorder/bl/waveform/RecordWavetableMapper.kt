package com.omar.retromp3recorder.bl.waveform

import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.repo.WavetableSampleRateRepo
import com.omar.retromp3recorder.utils.takeOne
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.absoluteValue

class RecordWavetableMapper @Inject constructor(
    private val recorder: Mp3VoiceRecorder,
    private val wavetableSampleRateRepo: WavetableSampleRateRepo
) {
    fun observe(): Observable<Byte> {
        return wavetableSampleRateRepo.observe().takeOne().flatMapObservable { rate ->
            recorder.observeRecorder()
                .map { array ->
                    array.toList().map { it.toInt().absoluteValue }.average()
                }
                .buffer(rate.value.toLong(), TimeUnit.MILLISECONDS)
                .map { it.average() }
                .map { it.toInt().toByte() }
        }
    }
}
