package com.omar.retromp3recorder.bl.waveform

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WaveformScannerSuspendTest {
    @Test
    fun `when file no length then crash`() = runTest { }

    @Test
    fun `when audio length less 100 millis then 10 takes per second`() = runTest { }

    @Test
    fun `when audio length more 1000 millis then 1 takes per second`() = runTest { }

    @Test
    fun `when audio length  between 1000 and 100 millis linear variable takes per second`() = runTest { }

    @Test
    fun `amplituda returns null then returns same file`() = runTest { }

    @Test
    fun `amplituda result windowed with window size reverse dependent to data size`() = runTest { }
}
