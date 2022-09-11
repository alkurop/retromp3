package com.omar.retromp3recorder.bl.waveform

import com.omar.retromp3recorder.bl.waveform.WavetableSummer.Companion.MAX_SIZE
import com.omar.retromp3recorder.dto.ExistingFileWrapper
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import io.reactivex.rxjava3.core.Single
import linc.com.amplituda.Amplituda
import linc.com.amplituda.Compress
import linc.com.amplituda.Compress.SKIP
import javax.inject.Inject

class WaveformScanner @Inject constructor() {
    fun execute(file: ExistingFileWrapper, amplituda: Amplituda): Single<ExistingFileWrapper> {
        return Single.create { source ->
            val lengthMillis = file.length!!
            val default = 1000 / Mp3VoiceRecorder.WaveTableSampleRate._100.value
            val lengthSeconds = lengthMillis / MAX_SIZE
            val takesPerSecond = when {
                lengthSeconds <= 100 -> default // less then a 100 seconds 10 sample per seconds 1000 samples
                lengthSeconds >= 1000 -> 1
                else -> {
                    1000 / lengthSeconds // between 100 seconds and 10000 seconds variable, max 1 sample per second, 1000 seconds
                }
            }.toInt()
            amplituda.processAudio(
                file.path, Compress.withParams(
                    SKIP,
                    takesPerSecond,
                )
            ).get({ success ->
                if (!source.isDisposed) {
                    source.onSuccess(file)
                }

            }, { error ->
                if (!source.isDisposed) {
                    source.onSuccess(file)
                }
            })
        }
    }
}