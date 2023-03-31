package com.omar.retromp3recorder.bl.waveform

import app.cash.turbine.test
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RecordWavetableMapperTest {
    private val recorder = mockk<Mp3VoiceRecorder>()
    private lateinit var tested: RecordWavetableMapper

    @Before
    fun setUp() {
        tested = RecordWavetableMapper(recorder)
    }

    @Test
    fun `When Recording idle THEN collection waveform stops`() = runTest {

        val recorderBus = MutableStateFlow(Mp3VoiceRecorder.State.Idle)


        val bytes = arrayOf(1, 1, 1, 1).map { it.toByte() }.toByteArray()
        val waveBus = MutableStateFlow(bytes)

        every { recorder.stateFlow() } returns recorderBus

        every { recorder.recorderFlow() } returns waveBus
        tested.flow().test {
            // after Idle state, collecting waveform should stop.
            // when waveform emits new event, it is ignored
            expectNoEvents()
        }
    }

    @Test
    fun `When Recording Recording THEN collect waveform`() = runTest {
        val recorderBus = MutableStateFlow(Mp3VoiceRecorder.State.Recording)
        val bytes = arrayOf(1, 1, 1, 1).map { it.toByte() }.toByteArray()
        val waveBus = MutableStateFlow(bytes)

        every { recorder.stateFlow() } returns recorderBus

        every { recorder.recorderFlow() } returns waveBus
        tested.flow().test {
            val expected = (1).toByte()
            assertEquals(expected, awaitItem())
        }
    }
}
