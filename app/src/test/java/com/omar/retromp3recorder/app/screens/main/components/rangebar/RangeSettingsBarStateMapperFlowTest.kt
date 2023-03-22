package com.omar.retromp3recorder.app.screens.main.components.rangebar

import app.cash.turbine.test
import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.bl.audio.progress.JoinedProgressMapper
import com.omar.retromp3recorder.data.mock.MockPlayerProgressFactory
import com.omar.retromp3recorder.domain.JoinedProgress
import com.omar.retromp3recorder.storage.repo.local.RangeBarResetBus
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RangeSettingsBarStateMapperFlowTest {
    private val joinedProgressMapper = mockk<JoinedProgressMapper>()
    private lateinit var rangeBarResetBus: RangeBarResetBus
    private lateinit var tested: RangeBarStateMapperFlow

    @Before
    fun setUp() {
        rangeBarResetBus = RangeBarResetBus()
        tested = RangeBarStateMapperFlow(
            joinedProgressRepo = joinedProgressMapper,
            rangeBarResetBus
        )
    }

    @Test
    fun `WHEN range active not THEN state hidden`() = runTest {
        val progress = MockPlayerProgressFactory.givePlayerProgress()
        every { joinedProgressMapper.flow() } returns flowOf(
            JoinedProgress.PlayerProgressShown(
                progress,
                null
            )
        )
        tested.flow().test {
            val item = awaitItem()
            assert(item is RangeBarView.State.Hidden)
        }
    }

    @Test
    fun `WHEN range active Audio State PlayerProgressShown THEN state visible`() = runTest {
        val progress = MockPlayerProgressFactory.givePlayerProgress(
            MockPlayerProgressFactory.giveRange(isVisible = true)
        )
        every { joinedProgressMapper.flow() } returns flowOf(
            JoinedProgress.PlayerProgressShown(
                progress,
                null
            )
        )
        tested.flow().test {
            val item = awaitItem()
            assert(item is RangeBarView.State.Visible)
        }
    }

    @Test
    fun `WHEN range active Audio State RecorderProgressShown THEN state hidden`() = runTest {
        every { joinedProgressMapper.flow() } returns flowOf(
            JoinedProgress.RecorderProgressShown(0, mockk())
        )
        tested.flow().test {
            val item = awaitItem()
            assert(item is RangeBarView.State.Hidden)
        }
    }

    @Test
    fun `WHEN range active Audio State Intermediate THEN state hidden`() = runTest {
        every { joinedProgressMapper.flow() } returns flowOf(
            JoinedProgress.Intermediate
        )
        tested.flow().test {
            val item = awaitItem()
            assert(item is RangeBarView.State.Hidden)
        }
    }

    @Test
    fun `WHEN range active Audio State Hidden THEN state hidden`() = runTest {
        every { joinedProgressMapper.flow() } returns flowOf(
            JoinedProgress.Hidden
        )
        tested.flow().test {
            val item = awaitItem()
            assert(item is RangeBarView.State.Hidden)
        }
    }

    @Test
    fun `WHEN reset active Event resent`() = runTest {
        val progress = MockPlayerProgressFactory.givePlayerProgress(
            MockPlayerProgressFactory.giveRange(isVisible = true)
        )
        val resetCount = 20
        rangeBarResetBus.emit(Shell(resetCount))

        every { joinedProgressMapper.flow() } returns flowOf(
            JoinedProgress.PlayerProgressShown(
                progress,
                null
            )
        )
        tested.flow().test {
            val item = awaitItem() as RangeBarView.State.Visible
            assertEquals(resetCount, item.reset.ghost)
        }

    }
}
