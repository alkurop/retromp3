package com.omar.retromp3recorder.bl.waveform

import com.omar.retromp3recorder.bl.waveform.WavetableSummer.Companion.MAX_WAVEFORM_SIZE_SECONDS
import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.utils.domain.AudioCoroutineContext
import com.omar.retromp3recorder.utils.platform.AmplitudaWaveformScanner
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WaveformScannerSuspend @Inject constructor(
    private val jobWrapper: AudioCoroutineContext,
    private val amplitudaDealer: AmplitudaWaveformScanner,
) {
    suspend fun execute(
        file: ExistingFileWrapper
    ): ExistingFileWrapper {
        val audioLength = requireNotNull(file.length) { "File length should not be null" }
        require(audioLength > 0) { "File length should not be 0" }
        val default = MAX_WAVEFORM_SIZE_SECONDS / MIN_SIZE_NO_COMPRESS
        val lengthSeconds = audioLength / MAX_WAVEFORM_SIZE_SECONDS.toFloat()
        val takesPerSecond = when {
            lengthSeconds <= MIN_SIZE_NO_COMPRESS -> default // less then a 100 seconds 10 sample per seconds 1000 samples
            lengthSeconds >= MAX_WAVEFORM_SIZE_SECONDS -> 1
            else -> MAX_WAVEFORM_SIZE_SECONDS / lengthSeconds /* between 100 seconds and 10000 seconds variable, max 1 sample per second, 1000 seconds*/
        }.toInt()

        val wavetable = withContext(jobWrapper.coroutineContext) {
            amplitudaDealer.scan(file.path, takesPerSecond, MAX_WAVEFORM_SIZE_SECONDS)
        }
        return if (wavetable == null) {
            file
        } else {
            file.copy(wavetable = wavetable)
        }
    }
}

private const val MIN_SIZE_NO_COMPRESS = 100
