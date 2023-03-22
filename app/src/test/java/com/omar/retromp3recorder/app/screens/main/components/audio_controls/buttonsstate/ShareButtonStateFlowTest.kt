package com.omar.retromp3recorder.app.screens.main.components.audio_controls.buttonsstate

import com.omar.retromp3recorder.app.screens.main.components.audio_controls.compose.InteractiveButtonState
import com.omar.retromp3recorder.bl.audio.progress.AudioState
import com.omar.retromp3recorder.bl.audio.progress.AudioStateMapper
import com.omar.retromp3recorder.bl.files.HasPlayableFileMapperFlow
import com.omar.retromp3recorder.utils.platform.Optional
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ShareButtonStateFlowTest{
    private val audioState = mockk<AudioStateMapper>()
    private val fileState = mockk<HasPlayableFileMapperFlow>()
    private lateinit var stateFlow: ShareButtonStateFlow

    @Before
    fun setUp() {
        every { fileState.flow() } returns flowOf(Optional.empty())
        stateFlow = ShareButtonStateFlow(audioState, fileState)
    }

    @Test
    fun `WHEN audio state Recording THEN button state Disabled`() = runTest {
        every { audioState.flow() } returns flowOf(AudioState.Recording)

        Assert.assertEquals(InteractiveButtonState.DISABLED, stateFlow.flow().first())
    }

    @Test
    fun `WHEN audio state Playing THEN button state Enabled`() = runTest {
        every { audioState.flow() } returns flowOf(AudioState.Playing)

        Assert.assertEquals(InteractiveButtonState.ENABLED, stateFlow.flow().first())
    }

    @Test
    fun `WHEN audio state Seek_Paused THEN button state Disabled`() = runTest {
        every { audioState.flow() } returns flowOf(AudioState.Seek_Paused)

        Assert.assertEquals(InteractiveButtonState.DISABLED, stateFlow.flow().first())
    }

    @Test
    fun `WHEN audio state Idle has file THEN button state Enabled`() = runTest {
        every { audioState.flow() } returns flowOf(AudioState.Idle)
        every { fileState.flow() } returns flowOf(Optional(mockk()))
        Assert.assertEquals(InteractiveButtonState.ENABLED, stateFlow.flow().first())
    }

    @Test
    fun `WHEN audio state Idle not has file THEN button state Disabled`() = runTest {
        every { audioState.flow() } returns flowOf(AudioState.Idle)

        Assert.assertEquals(InteractiveButtonState.DISABLED, stateFlow.flow().first())
    }
}
