package com.omar.retromp3recorder.bl.waveform

import com.omar.retromp3recorder.bl.waveform.WavetableSummer.Companion.MAX_WAVEFORM_SIZE
import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.domain.Wavetable
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.utils.platform.AmplitudaDealer
import io.reactivex.rxjava3.core.Single
import linc.com.amplituda.Compress
import linc.com.amplituda.Compress.SKIP
import javax.inject.Inject

class WaveformScanner @Inject constructor() {
    fun execute(file: ExistingFileWrapper, amplitudaDealer: AmplitudaDealer): Single<ExistingFileWrapper> {

        return Single.create { source ->
            val lengthMillis = file.length!!
            val default = MAX_WAVEFORM_SIZE / Mp3VoiceRecorder.WaveTableSampleRate._100.value
            val lengthSeconds = lengthMillis / MAX_WAVEFORM_SIZE
            val takesPerSecond = when {
                lengthSeconds <= 100 -> default // less then a 100 seconds 10 sample per seconds 1000 samples
                lengthSeconds >= MAX_WAVEFORM_SIZE -> 1
                else -> {
                    MAX_WAVEFORM_SIZE / lengthSeconds // between 100 seconds and 10000 seconds variable, max 1 sample per second, 1000 seconds
                }
            }.toInt()
            amplitudaDealer.createAmplituda().processAudio(
                file.path, Compress.withParams(
                    SKIP,
                    takesPerSecond,
                )
            ).get({ success ->
                if (!source.isDisposed) {
                    val data = success.amplitudesAsList()
                    val multiplier = 1 + data.size / MAX_WAVEFORM_SIZE
                    val res = data.windowed(multiplier, multiplier, true)
                        .map { list -> list.maxOrNull()?.times(2) ?: 0 }.toMutableList()
                    if (res.firstOrNull { it != 0 } == null) {
                        res.removeAt(0)
                        res.add(0, 1)
                    }
                    val size =
                        takesPerSecond * MAX_WAVEFORM_SIZE / Mp3VoiceRecorder.WaveTableSampleRate._100.value * multiplier

                    val wavetable = Wavetable(res.map { it.toByte() }.toByteArray(), size)
                    source.onSuccess(file.copy(wavetable = wavetable))
                }

            }, {
                if (!source.isDisposed) {
                    source.onSuccess(file)
                }
            })
        }
    }
}
