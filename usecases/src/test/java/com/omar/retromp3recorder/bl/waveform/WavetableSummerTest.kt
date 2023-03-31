package com.omar.retromp3recorder.bl.waveform

import com.omar.retromp3recorder.bl.waveform.WavetableSummer.Companion.MAX_WAVEFORM_SIZE_SECONDS
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class WavetableSummerTest {
    private lateinit var wavetableSummer: WavetableSummer

    @Before
    fun setup() {
        wavetableSummer = WavetableSummer()
    }

    @Test
    fun `sums items and always lower then waveform size`() {
        val wavetable =
            (1..MAX_WAVEFORM_SIZE_SECONDS * 2).toList().fold(WavetableSummer()) { summer, next ->
                WavetableSummer.reducer(
                    summer,
                    next.toByte()
                )
            }.toWaveTable()
        assertEquals(MAX_WAVEFORM_SIZE_SECONDS, wavetable.bytes.size)
    }


    @Test
    fun `averages items in batches`() {
        val list = (1..MAX_WAVEFORM_SIZE_SECONDS * 2).toList()
        val index = 38
        val interestingItems = listOf(list[index], list[index + 1])
        val expected = interestingItems.max()
        val wavetable = list.fold(WavetableSummer()) { summer, next ->
            WavetableSummer.reducer(
                summer,
                next.toByte()
            )
        }.toWaveTable()

        assertEquals(expected.toByte(), wavetable.bytes[index/2])
    }

}
