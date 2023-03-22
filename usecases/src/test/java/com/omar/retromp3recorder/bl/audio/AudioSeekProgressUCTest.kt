package com.omar.retromp3recorder.bl.audio

import com.omar.retromp3recorder.bl.audio.progress.AudioSeekProgressUC
import com.omar.retromp3recorder.storage.repo.local.PlayerProgressRepo
import io.mockk.mockk
import io.mockk.verifyOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AudioSeekProgressUCTest {
    private val repp = mockk<PlayerProgressRepo>(relaxed = true)
    private lateinit var tested: AudioSeekProgressUC

    @Before
    fun setUp() {
        tested = AudioSeekProgressUC(repp)
    }

    @Test
    fun `on execute repo seek emit`() = runTest {
        val position: Long = 99
        tested.execute(position = position)

        verifyOrder {
            repp.emit(PlayerProgressRepo.In.Seek(position))
        }

    }
}
