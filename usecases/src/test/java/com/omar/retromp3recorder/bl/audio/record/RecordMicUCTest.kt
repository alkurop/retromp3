package com.omar.retromp3recorder.bl.audio.record

import io.mockk.coVerifySequence
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RecordMicUCTest {
    private val micCaptureCompletableCreator = mockk<AudioCaptureUC>(relaxed = true)
    private val wavetableUC = mockk<RecordWavetableUCSuspend>(relaxed = true)

    private lateinit var tested: RecordMicUC

    @Before
    fun setUp() {
        tested = RecordMicUC(micCaptureCompletableCreator, wavetableUC)
    }

    @Test
    fun `on execute sequence executed`() = runTest {
        tested.execute()

        coVerifySequence {
            micCaptureCompletableCreator.execute(any())
            wavetableUC.execute()
        }
    }
}
