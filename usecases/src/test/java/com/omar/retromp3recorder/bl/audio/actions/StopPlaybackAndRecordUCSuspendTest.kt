package com.omar.retromp3recorder.bl.audio.actions

import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.bl.audio.progress.AudioState
import com.omar.retromp3recorder.bl.audio.progress.AudioStateMapper
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class StopPlaybackAndRecordUCSuspendTest {
    private val audioPlayer = mockk<AudioPlayer>(relaxed = true)
    private val stopRecord = mockk<StopRecordUCSuspend>(relaxed = true)
    private val audioStateMapper = mockk<AudioStateMapper>(relaxed = true)

    private lateinit var tested: StopPlaybackAndRecordUCSuspend

    @Before
    fun setUp() {
        tested = StopPlaybackAndRecordUCSuspend(audioPlayer, stopRecord, audioStateMapper)
    }

    @Test
    fun `when playing player stop`() = runTest {
        every { audioStateMapper.flow() } returns flowOf(AudioState.Playing)
        tested.execute()

        coVerify(exactly = 1) { audioPlayer.onInput(withArg { it is AudioPlayer.Input.Stop }) }
        coVerify(exactly = 0) { stopRecord.execute() }
    }

    @Test
    fun `when seek player stop`() = runTest {
        every { audioStateMapper.flow() } returns flowOf(AudioState.SeekPaused)
        tested.execute()

        coVerify(exactly = 1) { audioPlayer.onInput(withArg { it is AudioPlayer.Input.Stop }) }
        coVerify(exactly = 0) { stopRecord.execute() }
    }

    @Test
    fun `when recording recorder stop`() = runTest {
        every { audioStateMapper.flow() } returns flowOf(AudioState.Recording)
        tested.execute()

        coVerify(exactly = 0) { audioPlayer.onInput(any()) }
        coVerify(exactly = 1) { stopRecord.execute() }
    }

    @Test
    fun `idle do nothing`() = runTest {
        every { audioStateMapper.flow() } returns flowOf(AudioState.Idle)
        tested.execute()

        coVerify(exactly = 0) { audioPlayer.onInput(any()) }
        coVerify(exactly = 0) { stopRecord.execute() }
    }
}
