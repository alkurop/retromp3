package com.omar.retromp3recorder.bl.audio.progress

import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.bl.audio.actions.StartPlaybackUC
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AudioSeekFinishUCTest {
    private val audioPlayer = mockk<AudioPlayer>(relaxed = true)
    private val startPlaybackUC = mockk<StartPlaybackUC>(relaxed = true)

    private lateinit var tested: AudioSeekFinishUC

    @Before
    fun setUp() {
        tested = AudioSeekFinishUC(audioPlayer, startPlaybackUC)
    }

    @Test
    fun `on playing executed`() = runTest {
        every { audioPlayer.observeState() } returns Observable.just(AudioPlayer.State.Playing)
        tested.execute()
        verify { startPlaybackUC.execute() }
    }

    @Test
    fun `on seek executed`() = runTest {
        every { audioPlayer.observeState() } returns Observable.just(AudioPlayer.State.PausedToSeek)
        tested.execute()
        verify { startPlaybackUC.execute() }
    }

    @Test
    fun `on idle do nothing`() = runTest {
        every { audioPlayer.observeState() } returns Observable.just(AudioPlayer.State.Idle)
        tested.execute()
        verify(exactly = 0) { startPlaybackUC.execute() }
    }
}
