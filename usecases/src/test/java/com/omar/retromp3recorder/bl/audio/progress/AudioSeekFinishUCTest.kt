package com.omar.retromp3recorder.bl.audio.progress

import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.bl.audio.actions.StartPlaybackUCSuspend
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AudioSeekFinishUCTest {
    private val audioPlayer = mockk<AudioPlayer>(relaxed = true)
    private val startPlaybackUC = mockk<StartPlaybackUCSuspend>(relaxed = true)

    private lateinit var tested: AudioSeekFinishUC

    @Before
    fun setUp() {
        tested = AudioSeekFinishUC(audioPlayer, startPlaybackUC)
    }

    @Test
    fun `on playing executed`() = runTest {
        every { audioPlayer.stateFlow() } returns flowOf(AudioPlayer.State.Playing)
        tested.execute()
        coVerify { startPlaybackUC.execute() }
    }

    @Test
    fun `on seek executed`() = runTest {
        every { audioPlayer.stateFlow() } returns flowOf(AudioPlayer.State.PausedToSeek)
        tested.execute()
        coVerify { startPlaybackUC.execute() }
    }

    @Test
    fun `on idle do nothing`() = runTest {
        every { audioPlayer.stateFlow() } returns flowOf(AudioPlayer.State.Idle)
        tested.execute()
        coVerify(exactly = 0) { startPlaybackUC.execute() }
    }
}
