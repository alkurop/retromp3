package com.omar.retromp3recorder.app.screens.main.components.audio_controls.buttonsstate

import com.omar.retromp3recorder.app.screens.main.components.audio_controls.compose.InteractiveButtonState
import com.omar.retromp3recorder.bl.audio.progress.AudioState
import com.omar.retromp3recorder.bl.audio.progress.AudioStateMapperFlow
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class StopButtonStateMapperFlowTest{
    private val audioState = mockk<AudioStateMapperFlow>()
    private lateinit var stateFlow: StopButtonStateMapperFlow

    @Before
    fun setUp() {
        stateFlow = StopButtonStateMapperFlow(audioState)
    }

    @Test
    fun `WHEN audio state Recording THEN button state Enabled`() = runTest {
        every { audioState.flow() } returns flowOf(AudioState.Recording)

        assertEquals(InteractiveButtonState.ENABLED, stateFlow.flow().first())
    }

    @Test
    fun `WHEN audio state Playing THEN button state Enabled`() = runTest {
        every { audioState.flow() } returns flowOf(AudioState.Playing)

        assertEquals(InteractiveButtonState.ENABLED, stateFlow.flow().first())
    }

    @Test
    fun `WHEN audio state Seek_Paused THEN button state Disabled`() = runTest {
        every { audioState.flow() } returns flowOf(AudioState.Seek_Paused)

        assertEquals(InteractiveButtonState.ENABLED, stateFlow.flow().first())
    }


    @Test
    fun `WHEN audio state Idle THEN button state Disabled`() = runTest {
        every { audioState.flow() } returns flowOf(AudioState.Idle)

        assertEquals(InteractiveButtonState.DISABLED, stateFlow.flow().first())
    }
}
