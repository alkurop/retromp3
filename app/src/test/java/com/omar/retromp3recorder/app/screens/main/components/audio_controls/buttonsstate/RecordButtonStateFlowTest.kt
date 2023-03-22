package com.omar.retromp3recorder.app.screens.main.components.audio_controls.buttonsstate

import com.omar.retromp3recorder.app.screens.main.components.audio_controls.compose.InteractiveButtonState
import com.omar.retromp3recorder.bl.audio.progress.AudioState
import com.omar.retromp3recorder.bl.audio.progress.AudioStateMapper
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
class RecordButtonStateFlowTest {
    private val audioState = mockk<AudioStateMapper>()
    private lateinit var stateFlow: RecordButtonStateFlow

    @Before
    fun setUp() {
        stateFlow = RecordButtonStateFlow(audioState)
    }

    @Test
    fun `WHEN audio state Recording THEN button state Running`() = runTest {
        every { audioState.observe() } returns Observable.just(AudioState.Recording)

        Assert.assertEquals(InteractiveButtonState.RUNNING, stateFlow.flow().first())
    }

    @Test
    fun `WHEN audio state Playing THEN button state Disabled`() = runTest {
        every { audioState.observe() } returns Observable.just(AudioState.Playing)

        Assert.assertEquals(InteractiveButtonState.DISABLED, stateFlow.flow().first())
    }

    @Test
    fun `WHEN audio state Seek_Paused THEN button state Disabled`() = runTest {
        every { audioState.observe() } returns Observable.just(AudioState.Seek_Paused)

        Assert.assertEquals(InteractiveButtonState.DISABLED, stateFlow.flow().first())
    }


    @Test
    fun `WHEN audio state Idle THEN button state ENABLED`() = runTest {
        every { audioState.observe() } returns Observable.just(AudioState.Idle)

        Assert.assertEquals(InteractiveButtonState.ENABLED, stateFlow.flow().first())
    }
}
