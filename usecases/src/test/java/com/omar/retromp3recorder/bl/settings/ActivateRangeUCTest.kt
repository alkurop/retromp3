package com.omar.retromp3recorder.bl.settings

import com.omar.retromp3recorder.domain.PlayerControls
import com.omar.retromp3recorder.storage.repo.local.PlayerControlsRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ActivateRangeUCTest {
    private val repo = PlayerControlsRepo()
    private lateinit var tested: ActivateRangeUC

    @Before
    fun setUp() {
        tested = ActivateRangeUC(repo)
    }

    @Test
    fun `on execute emit to repo player controls with changed range activate setting`() = runTest {
        val originalSettings = PlayerControls()
        val flag = originalSettings.range.isActive.not()
        val newRangeSettings = originalSettings.range.copy(isActive = flag)
        val expectedSettings = originalSettings.copy(range = newRangeSettings)

        repo.emit(originalSettings)
        tested.execute()
        assertEquals(expectedSettings, repo.first())
    }
}
