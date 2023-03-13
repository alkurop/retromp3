package com.omar.retromp3recorder.app.screens.main.components.audio_controls.buttonsstate

import com.omar.retromp3recorder.app.screens.main.components.audio_controls.compose.InteractiveButtonState
import com.omar.retromp3recorder.bl.audio.AudioState
import com.omar.retromp3recorder.bl.audio.AudioStateMapper
import com.omar.retromp3recorder.bl.files.HasPlayableFileMapper
import com.omar.retromp3recorder.utils.platform.Optional
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ShareButtonStateFlowTest{
    private val audioState = mockk<AudioStateMapper>()
    private val fileState = mockk<HasPlayableFileMapper>()
    private lateinit var stateFlow: ShareButtonStateFlow

    @Before
    fun setUp() {
        every { fileState.observe() } returns Observable.just(Optional.empty())
        stateFlow = ShareButtonStateFlow(audioState, fileState)
    }

    @Test
    fun `WHEN audio state Recording THEN button state Disabled`() = runTest {
        every { audioState.observe() } returns Observable.just(AudioState.Recording)

        Assert.assertEquals(InteractiveButtonState.DISABLED, stateFlow.flow().first())
    }

    @Test
    fun `WHEN audio state Playing THEN button state Enabled`() = runTest {
        every { audioState.observe() } returns Observable.just(AudioState.Playing)

        Assert.assertEquals(InteractiveButtonState.ENABLED, stateFlow.flow().first())
    }

    @Test
    fun `WHEN audio state Seek_Paused THEN button state Disabled`() = runTest {
        every { audioState.observe() } returns Observable.just(AudioState.Seek_Paused)

        Assert.assertEquals(InteractiveButtonState.DISABLED, stateFlow.flow().first())
    }

    @Test
    fun `WHEN audio state Idle has file THEN button state Enabled`() = runTest {
        every { audioState.observe() } returns Observable.just(AudioState.Idle)
        every { fileState.observe() } returns Observable.just(Optional(mockk()))
        Assert.assertEquals(InteractiveButtonState.ENABLED, stateFlow.flow().first())
    }

    @Test
    fun `WHEN audio state Idle not has file THEN button state Disabled`() = runTest {
        every { audioState.observe() } returns Observable.just(AudioState.Idle)

        Assert.assertEquals(InteractiveButtonState.DISABLED, stateFlow.flow().first())
    }
}
