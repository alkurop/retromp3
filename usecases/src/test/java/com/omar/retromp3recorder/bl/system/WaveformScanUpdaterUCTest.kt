package com.omar.retromp3recorder.bl.system

import com.omar.retromp3recorder.bl.database.DbUpdaterUCSuspend
import com.omar.retromp3recorder.bl.waveform.WaveformScannerSuspend
import com.omar.retromp3recorder.data.mock.MockFileFactory
import com.omar.retromp3recorder.data.mock.MockWaveformFactory
import com.omar.retromp3recorder.utils.domain.DifferedCoroutineScope
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
@Suppress("UNUSED")
class WaveformScanUpdaterUCTest {
    @MockK(relaxed = true)
    private lateinit var dbUpdaterUC: DbUpdaterUCSuspend

    @MockK(relaxed = true)
    private lateinit var waveformScanner: WaveformScannerSuspend

    private val differedCoroutineScope = DifferedCoroutineScope(UnconfinedTestDispatcher())

    @InjectMockKs
    private lateinit var tested: WaveformScanUpdaterUC

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `when input is empty do nothing`() = runTest {
        tested.execute(emptyList())
        coVerify(exactly = 0) { waveformScanner.execute(any()) }
        coVerify(exactly = 0) { dbUpdaterUC.execute(any()) }
    }

    @Test
    fun `for each item with empty waveform, scanner executed, db updated`() = runTest {
        val list = (1..10).toList().map { MockFileFactory.giveExistingFile() }

        tested.execute(list)

        coVerify(exactly = list.size) { waveformScanner.execute(any()) }
        coVerify(exactly = 1) { dbUpdaterUC.execute(any()) }
    }

    @Test
    fun `items with nonempty waveform are ignored`() = runTest {
        val list = (1..10).toList().map {
            MockFileFactory.giveExistingFile().copy(wavetable = MockWaveformFactory.giveWaveform())
        }
        tested.execute(list)

        coVerify(exactly = 0) { waveformScanner.execute(any()) }
        coVerify(exactly = 0) { dbUpdaterUC.execute(any()) }
    }
}
