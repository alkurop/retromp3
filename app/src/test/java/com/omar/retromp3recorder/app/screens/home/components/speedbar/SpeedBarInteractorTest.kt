package com.omar.retromp3recorder.app.screens.home.components.speedbar

import app.cash.turbine.test
import com.omar.retromp3recorder.bl.audio.effects.PlaybackSpeedEnabledUC
import com.omar.retromp3recorder.bl.audio.effects.PlaybackSpeedSetUC
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
    private val speedBarStateMapper: SpeedBarStateMapper = mockk()
    private val playbackSpeedSetUC: PlaybackSpeedSetUC = mockk()
    private val setSpeedEnabled: PlaybackSpeedEnabledUC = mockk()
    private lateinit var tested: SpeedBarInteractor

    @Before
    fun setup() {
        tested = SpeedBarInteractor(
            speedBarStateMapper, playbackSpeedSetUC, setSpeedEnabled, UnconfinedTestDispatcher()
        )
    }

    @Test
    fun `speed bar state mapper listened`() = runTest {
        val value = mockk<SpeedBarContract.State.Visible>()
        every { speedBarStateMapper.flow() } returns flowOf(value)
        tested.processIO(emptyFlow()).test {
            val item = awaitItem()
            assertEquals(value, item)
            awaitComplete()
        }
    }

    @Test
    fun `on SpeedSet input set speed executed`() = runTest {
        every { speedBarStateMapper.flow() } returns emptyFlow()
        val value = SpeedBarContract.Input.SpeedSet(2f)
        tested.processIO(flowOf(value)).test {
            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 1) { playbackSpeedSetUC.execute(value.speed) }
        coVerify(exactly = 0) { setSpeedEnabled.execute() }
    }

    @Test
    fun `on SpeedEnable input enable speed executed`() = runTest {
        every { speedBarStateMapper.flow() } returns emptyFlow()
        tested.processIO(flowOf(SpeedBarContract.Input.Enable)).test {
            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 0) { playbackSpeedSetUC.execute(any()) }
        coVerify(exactly = 1) { setSpeedEnabled.execute() }
    }
}
