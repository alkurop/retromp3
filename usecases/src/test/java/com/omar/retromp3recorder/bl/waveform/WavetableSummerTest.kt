package com.omar.retromp3recorder.bl.waveform

import com.omar.retromp3recorder.bl.waveform.WavetableSummer.Companion.MAX_WAVEFORM_SIZE
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
            (1..MAX_WAVEFORM_SIZE * 2).toList().fold(WavetableSummer()) { summer, next ->
                WavetableSummer.reducer(
                    summer,
                    next.toByte()
                )
            }.toWaveTable()
        assertEquals(MAX_WAVEFORM_SIZE, wavetable.data.size)
    }


    @Test
    fun `averages items in batches`() {
        val list = (1..MAX_WAVEFORM_SIZE * 2).toList()
        val index = 38
        val interestingItems = listOf(list[index], list[index + 1])
        val expected = interestingItems.max()
        val wavetable = list.fold(WavetableSummer()) { summer, next ->
            WavetableSummer.reducer(
                summer,
                next.toByte()
            )
        }.toWaveTable()

        assertEquals(expected.toByte(), wavetable.data[index/2])
    }

    @Test
    fun `test reducer`() {
        val summer1 = WavetableSummer()
        val summer2 = WavetableSummer.reducer(summer1, 2)
        val summer3 = WavetableSummer.reducer(summer2, 4)
        val summer4 = WavetableSummer.reducer(summer3, 6)
        assertEquals(3, summer4.getProgress())
    }
}
