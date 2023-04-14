package com.omar.retromp3recorder.app.screens.home.components.speedbar

import app.cash.turbine.test
import com.omar.retromp3recorder.bl.audio.effects.PlaybackSpeedEnabledUC
import com.omar.retromp3recorder.bl.audio.effects.PlaybackSpeedSetUC
import com.omar.retromp3recorder.domain.PlayerControls
import com.omar.retromp3recorder.storage.repo.local.PlayerControlsRepo
import com.omar.retromp3recorder.utils.domain.DifferedCoroutineScope
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SpeedBarInteractorTest {
    private val speedBarVisibilityMapper: SpeedBarVisibilityMapper = mockk()
    private val playbackSpeedSetUC: PlaybackSpeedSetUC = mockk()
    private val setSpeedEnabled: PlaybackSpeedEnabledUC = mockk()
    private lateinit var playerControlsRepo: PlayerControlsRepo
    private lateinit var tested: SpeedBarInteractor

    @Before
    fun setup() {
        playerControlsRepo = PlayerControlsRepo()
        tested = SpeedBarInteractor(
            speedBarVisibilityMapper,
            playbackSpeedSetUC,
            setSpeedEnabled,
            playerControlsRepo,
            UnconfinedTestDispatcher()
        )
    }

    @Test
    fun `speed bar visibility state mapper listened`() = runTest {
        val value = SpeedBarContract.Output.Visibility(isVisible = true)

        every { speedBarVisibilityMapper.flow() } returns flowOf(true)

        tested.processIO(DifferedCoroutineScope(UnconfinedTestDispatcher()), emptyFlow()).test {
            skipItems(1)
            val item = awaitItem()
            assertEquals(value, item)
        }
    }

    @Test
    fun `speed bar player controls listened`() = runTest {
        val speedSettings = PlayerControls.SpeedSettings(isVisible = true)
        val value = SpeedBarContract.Output.SpeedDataUpdate(speedSettings)
        playerControlsRepo.emit(PlayerControls(speedSettings = speedSettings))

        every { speedBarVisibilityMapper.flow() } returns flowOf(true)
        tested.processIO(DifferedCoroutineScope(UnconfinedTestDispatcher()), emptyFlow()).test {
            val item = awaitItem()
            assertEquals(value, item)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `on SpeedSet input set speed executed`() = runTest {
        every { speedBarVisibilityMapper.flow() } returns emptyFlow()
        val value = SpeedBarContract.Input.SpeedSet(2f)
        tested.processIO(DifferedCoroutineScope(UnconfinedTestDispatcher()), flowOf(value)).test {
            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 1) { playbackSpeedSetUC.execute(value.speed) }
        coVerify(exactly = 0) { setSpeedEnabled.execute() }
    }

    @Test
    fun `on SpeedEnable input enable speed executed`() = runTest {
        every { speedBarVisibilityMapper.flow() } returns emptyFlow()
        tested.processIO(
            DifferedCoroutineScope(UnconfinedTestDispatcher()),
            flowOf(SpeedBarContract.Input.Enable)
        ).test {
            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 0) { playbackSpeedSetUC.execute(any()) }
        coVerify(exactly = 1) { setSpeedEnabled.execute() }
    }
}
