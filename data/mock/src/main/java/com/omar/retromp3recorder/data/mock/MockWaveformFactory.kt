package com.omar.retromp3recorder.data.mock

import com.omar.retromp3recorder.domain.Wavetable
import kotlin.random.nextUInt

object MockWaveformFactory {
    fun giveWaveform(): Wavetable {
        return Wavetable(
            (0..kotlin.random.Random.nextUInt(100u).toInt()).toList().map { it.toByte() }
                .toByteArray(),
            kotlin.random.Random.nextUInt(100u).toInt()
        )
    }
}
