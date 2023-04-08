package com.omar.retromp3recorder.utils.platform

import android.content.Context
import com.omar.retromp3recorder.domain.Wavetable
import com.omar.retromp3recorder.utils.domain.Optional
import com.omar.retromp3recorder.utils.domain.toOptional
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import linc.com.amplituda.Amplituda
import linc.com.amplituda.AmplitudaResult
import linc.com.amplituda.Compress
import timber.log.Timber
import javax.inject.Inject

class AmplitudaWaveformScanner @Inject constructor(
    @ApplicationContext private val context: Context
) {

    /**
     * @param takesPerSecond - how often does a scanner snapshot the waveform (typically 10 times per second)
     * @param maxSize - how many byte items in waveform can be (typically 1000)
     */
    suspend fun scan(
        filePath: String,
        takesPerSecond: Int,
        maxSize: Int,
    ): Wavetable? {

        val data = getAmplitudaResult(filePath, takesPerSecond) ?: return null
        val multiplier = 1 + data.size / maxSize
        val res = data
            .windowed(multiplier, multiplier, true)
            .map { list -> list.maxOrNull()?.times(3) ?: 0 }
            .toMutableList()

        return Wavetable(res.map { it.coerceAtMost(Byte.MAX_VALUE.toInt()).toByte() }.toByteArray())
    }

    private fun createAmplituda(): Amplituda {
        return Amplituda(context)
    }

    private suspend fun getAmplitudaResult(path: String, takesPerSecond: Int): List<Int>? {
        return createAmplituda().flow(path, takesPerSecond).first().value?.amplitudesAsList()
    }


    private fun Amplituda.flow(path: String, takesPerSecond: Int) =
        callbackFlow<Optional<AmplitudaResult<String>>> {
            processAudio(
                path, Compress.withParams(Compress.PEEK, takesPerSecond)
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
}
