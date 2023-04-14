package com.omar.retromp3recorder.app.screens.home.components.rangebar

import app.cash.turbine.test
import com.omar.retromp3recorder.bl.audio.actions.UpdatePlayerRangeUC
import com.omar.retromp3recorder.bl.settings.ActivateRangeUC
import com.omar.retromp3recorder.data.mock.MockPlayerProgressFactory
import com.omar.retromp3recorder.utils.domain.DifferedCoroutineScope
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RangeSettingsBarInteractorFlowTest {
    private val rangeStateMapper = mockk<RangeBarStateMapper>(relaxed = true)
    private val updatePlayerRangeUC = mockk<UpdatePlayerRangeUC>(relaxed = true)
    private val rangeEnableRangeUC = mockk<ActivateRangeUC>(relaxed = true)
    private val dispatcher = UnconfinedTestDispatcher()

    private lateinit var tested: RangeBarInteractor

    @Before
    fun setUp() {
        tested = RangeBarInteractor(
            rangeStateMapper, updatePlayerRangeUC, rangeEnableRangeUC, dispatcher
        )
    }

    @Test
    fun `on RangeSet input update rangeUpdate UC executed`() = runTest {
        val event = RangeBarView.Input.RangeSet(MockPlayerProgressFactory.giveRange())
        tested.processIO(DifferedCoroutineScope(dispatcher),flowOf(event)).test { cancelAndIgnoreRemainingEvents() }

        coVerify { updatePlayerRangeUC.execute(event.range) }
        coVerify(exactly = 0) { rangeEnableRangeUC.execute() }
    }

    @Test
    fun `on Enable input update rangeEnable UC executed`() = runTest {
        val event = RangeBarView.Input.Enable
        tested.processIO(DifferedCoroutineScope(dispatcher),flowOf(event)).test { cancelAndIgnoreRemainingEvents() }

        coVerify(exactly = 0) { updatePlayerRangeUC.execute(any()) }
        coVerify(exactly = 1) { rangeEnableRangeUC.execute() }
    }

    @Test
    fun `listen range state mapper`() = runTest {
        val event = mockk<RangeBarView.State>()
        coEvery { rangeStateMapper.flow() } returns flowOf(event)
        tested.processIO(DifferedCoroutineScope(dispatcher),flowOf()).test {
            val item = awaitItem()
            assertEquals(item, event)
            cancelAndIgnoreRemainingEvents()
        }
    }

}
