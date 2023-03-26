package com.omar.retromp3recorder.bl.settings

import com.omar.retromp3recorder.domain.PlayerControls
import com.omar.retromp3recorder.storage.repo.local.PlayerControlsRepo
import com.omar.retromp3recorder.utils.domain.repo.first
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ActivateRangeUCTestSettings {
    private val repo = PlayerControlsRepo()
    private lateinit var tested: ActivateRangeUC

    @Before
    fun setUp() {
        tested = ActivateRangeUC(repo)
    }

    @Test
    fun `on execute emit to repo player controls with changed range activate setting`() = runTest {
        val originalSettings = PlayerControls()
        val flag = originalSettings.rangeSettings.isActive.not()
        val newRangeSettings = originalSettings.rangeSettings.copy(isActive = flag)
        val expectedSettings = originalSettings.copy(rangeSettings = newRangeSettings)

        repo.emit(originalSettings)
        tested.execute()
        assertEquals(expectedSettings, repo.first())
    }
}
