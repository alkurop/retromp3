package com.omar.retromp3recorder.bl.waveform

import app.cash.turbine.test
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RecordWavetableMapperFlowTest {
    private val recorder = mockk<Mp3VoiceRecorder>()
    private lateinit var tested: RecordWavetableMapperFlow

    @Before
    fun setUp() {
        tested = RecordWavetableMapperFlow(recorder)
    }

    @Test
    fun `When Recording then idle THEN collection waveform stops (bug)`() = runTest {

        val recorderBus = MutableStateFlow(Mp3VoiceRecorder.State.Recording)


        val bytes = arrayOf(1, 1, 1, 1).map { it.toByte() }.toByteArray()
        val byte2 = arrayOf(3, 3, 3, 3).map { it.toByte() }.toByteArray()
        val waveBus = MutableStateFlow(bytes)

        every { recorder.stateFlow() } returns recorderBus

        every { recorder.recorderFlow() } returns waveBus
        tested.flow().test {
            recorderBus.emit(Mp3VoiceRecorder.State.Idle)
            waveBus.emit(byte2)

            // after Idle state, collecting waveform should stop.
            // when waveform emits new event, it is ignored
            expectNoEvents()
        }
    }
}
