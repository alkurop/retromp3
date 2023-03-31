package com.omar.retromp3recorder.bl.waveform

import com.omar.retromp3recorder.data.mock.MockFileFactory
import com.omar.retromp3recorder.data.mock.MockWaveformFactory
import com.omar.retromp3recorder.utils.domain.ScopeJobWrapper
import com.omar.retromp3recorder.utils.platform.AmplitudaWaveformScanner
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AmplitudaWaveformScannerSuspendTest {
    private val jobWrapper = ScopeJobWrapper(UnconfinedTestDispatcher())
    private val waveformScanner = mockk<AmplitudaWaveformScanner>()
    private lateinit var tested: WaveformScannerSuspend

    @Before
    fun setup() {
        tested = WaveformScannerSuspend(jobWrapper, waveformScanner)

    }

    @Test(expected = IllegalArgumentException::class)
    fun `when file no length then crash`() = runTest {
        val file = MockFileFactory.giveExistingFile().copy(length = null)
        tested.execute(file)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `when file zero length then crash`() = runTest {
        val file = MockFileFactory.giveExistingFile().copy(length = 0)
        tested.execute(file)
    }

    @Test
    fun `when audio length less equals 100 seconds then 10 takes per second`() = runTest {
        val expectedTakes = 10
        val length = 100_000L
        val file = MockFileFactory.giveExistingFile().copy(length = length)

        coEvery {
            waveformScanner.scan(
                filePath = any(),
                takesPerSecond = any(),
                maxSize = any()
            )
        } returns null

        tested.execute(file)

        coVerify {
            waveformScanner.scan(filePath = any(), takesPerSecond = withArg {
                assertEquals(it, expectedTakes)
            }, maxSize = any())
        }
    }

    @Test
    fun `when audio length more 1000 seconds then 1 take per second`() = runTest {
        val length = 1_000_001L
        val file = MockFileFactory.giveExistingFile().copy(length = length)

        coEvery {
            waveformScanner.scan(
                filePath = any(),
                takesPerSecond = any(),
                maxSize = any()
            )
        } returns null

        tested.execute(file)

        coVerify {
            waveformScanner.scan(filePath = any(), takesPerSecond = withArg {

                assertEquals(1, it)
            }, maxSize = any())
        }
    }

    @Test
    fun `when audio length  between 1000 and 100 millis linear variable takes per second`() =
        runTest {
            val length = 100_001L
            val file = MockFileFactory.giveExistingFile().copy(length = length)

            coEvery {
                waveformScanner.scan(
                    filePath = any(),
                    takesPerSecond = any(),
                    maxSize = any()
                )
            } returns null

            tested.execute(file)

            coVerify {
                waveformScanner.scan(filePath = any(), takesPerSecond = withArg {

                    assert(it in (2..9))
                }, maxSize = any())
            }
        }

    @Test
    fun `amplituda returns null then returns same file`() = runTest {
        val length = 100_001L
        val file = MockFileFactory.giveExistingFile().copy(length = length)
        coEvery {
            waveformScanner.scan(
                filePath = any(),
                takesPerSecond = any(),
                maxSize = any()
            )
        } returns null
        val result = tested.execute(file)
        assertEquals(tested, result)
    }

    @Test
    fun `amplituda result result the waveform replaced`() = runTest {
        val length = 100_001L
        val file = MockFileFactory.giveExistingFile().copy(length = length)
        val waveform = MockWaveformFactory.giveWaveform()
        val expected = file.copy(wavetable = waveform)

        coEvery {
            waveformScanner.scan(
                filePath = any(),
                takesPerSecond = any(),
                maxSize = any()
            )
        } returns waveform
        val result = tested.execute(file)
        assertEquals(expected, result)
    }
}
