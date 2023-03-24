package com.omar.retromp3recorder.bl.audio.progress

import com.omar.retromp3recorder.audioplayer.AudioPlayer
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AudioSeekPauseUCTest {
    private val audioPlayer = mockk<AudioPlayer>(relaxed = true)

    private lateinit var tested: AudioSeekPauseUC

    @Before
    fun setUp() {
        tested = AudioSeekPauseUC(audioPlayer)
    }

    @Test
    fun `on playing executed`() = runTest {
        every { audioPlayer.observeState() } returns Observable.just(AudioPlayer.State.Playing)
        tested.execute()
        verify { audioPlayer.onInput(AudioPlayer.Input.SeekPause) }
    }

    @Test
    fun `on seek executed`() = runTest {
        every { audioPlayer.observeState() } returns Observable.just(AudioPlayer.State.PausedToSeek)
        tested.execute()
        verify(exactly = 0) { audioPlayer.onInput(AudioPlayer.Input.SeekPause) }
    }

    @Test
    fun `on idle do nothing`() = runTest {
        every { audioPlayer.observeState() } returns Observable.just(AudioPlayer.State.Idle)
        tested.execute()
        verify(exactly = 0) { audioPlayer.onInput(AudioPlayer.Input.SeekPause) }
    }
}
