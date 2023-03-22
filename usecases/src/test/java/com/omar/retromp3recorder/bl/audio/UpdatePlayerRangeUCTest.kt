package com.omar.retromp3recorder.bl.audio

import com.omar.retromp3recorder.bl.audio.actions.UpdatePlayerRangeUC
import com.omar.retromp3recorder.data.mock.MockPlayerProgressFactory
import com.omar.retromp3recorder.storage.repo.local.PlayerProgressRepo
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UpdatePlayerRangeUCTest {
    private val repo = mockk<PlayerProgressRepo>(relaxed = true)
    private lateinit var tested: UpdatePlayerRangeUC

    @Before
    fun setUp() {
        tested = UpdatePlayerRangeUC(repo)
    }

    @Test
    fun `on execute range emitted to repo`() = runTest {
        val range = MockPlayerProgressFactory.giveRange()

        tested.execute(range)

        coVerify { repo.emit(PlayerProgressRepo.In.Range(range)) }

    }
}
