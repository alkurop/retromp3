package com.omar.retromp3recorder.bl.waveform

import com.omar.retromp3recorder.bl.waveform.WavetableSummer.Companion.MAX_SIZE
import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.domain.Wavetable
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.utils.platform.AmplitudaDealer
import com.omar.retromp3recorder.utils.domain.Optional
import com.omar.retromp3recorder.utils.domain.ScopeJobWrapper
import com.omar.retromp3recorder.utils.domain.toOptional
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import linc.com.amplituda.Amplituda
import linc.com.amplituda.AmplitudaResult
import linc.com.amplituda.Compress
import linc.com.amplituda.Compress.SKIP
import timber.log.Timber
import javax.inject.Inject

class WaveformScannerSuspend @Inject constructor(
    private val jobWrapper: ScopeJobWrapper
) {
    suspend fun execute(
        file: ExistingFileWrapper, amplitudaDealer: AmplitudaDealer
    ): ExistingFileWrapper {
        val lengthMillis = file.length!!
        val default = MAX_SIZE / Mp3VoiceRecorder.WaveTableSampleRate._100.value
        val lengthSeconds = lengthMillis / MAX_SIZE
        val takesPerSecond = when {
            lengthSeconds <= 100 -> default // less then a 100 seconds 10 sample per seconds 1000 samples
            lengthSeconds >= MAX_SIZE -> 1
            else -> {
                MAX_SIZE / lengthSeconds /* between 100 seconds and 10000 seconds variable, max 1 sample per second, 1000 seconds*/
            }
        }.toInt()

        val result = withContext(jobWrapper.coroutineContext) {
            amplitudaDealer.createAmplituda().flow(file.path, takesPerSecond).first().value
        }
        return if (result == null) {
            file
        } else {
            val data = result.amplitudesAsList()
            val multiplier = 1 + data.size / MAX_SIZE
            val res = data.windowed(multiplier, multiplier, true)
                .map { list -> list.maxOrNull()?.times(2) ?: 0 }.toMutableList()
            if (res.firstOrNull { it != 0 } == null) {
                res.removeAt(0)
                res.add(0, 1)
            }
            val size =
                takesPerSecond * MAX_SIZE / Mp3VoiceRecorder.WaveTableSampleRate._100.value * multiplier

            val wavetable = Wavetable(res.map { it.toByte() }.toByteArray(), size)
            file.copy(wavetable = wavetable)
        }
    }
}

private fun Amplituda.flow(path: String, takesPerSecond: Int) =
    callbackFlow<Optional<AmplitudaResult<String>>> {
        processAudio(
            path, Compress.withParams(SKIP, takesPerSecond)
        ).get({ success ->
            trySendBlocking(success.toOptional()).onFailure { throwable ->
                Timber.e(throwable)
            }
            channel.close()
        }, { error ->
            Timber.e(error)
            trySendBlocking(Optional.empty()).onFailure { throwable ->
                Timber.e(throwable)
            }
            channel.close()
        })
    }
