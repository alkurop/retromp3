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
    private val rangeStateMapper = mockk<RangeBarContentMapper>(relaxed = true)
    private val updatePlayerRangeUC = mockk<UpdatePlayerRangeUC>(relaxed = true)
    private val rangeEnableRangeUC = mockk<ActivateRangeUC>(relaxed = true)
    private val dispatcher = UnconfinedTestDispatcher()
    private val visibilityMapper: RangeBarVisibilityMapper = mockk(relaxed = true)

    private lateinit var tested: RangeBarInteractor

    @Before
    fun setUp() {
        tested = RangeBarInteractor(
            rangeStateMapper, updatePlayerRangeUC, rangeEnableRangeUC, visibilityMapper, dispatcher
        )
    }

    @Test
    fun `on RangeSet input update rangeUpdate UC executed`() = runTest {
        val event = RangeBarContract.Input.RangeSet(MockPlayerProgressFactory.giveRange())
        tested.processIO(flowOf(event))
            .test { cancelAndIgnoreRemainingEvents() }

        coVerify { updatePlayerRangeUC.execute(event.range) }
        coVerify(exactly = 0) { rangeEnableRangeUC.execute() }
    }

    @Test
    fun `on Enable input update rangeEnable UC executed`() = runTest {
        val event = RangeBarContract.Input.Enable
        tested.processIO(flowOf(event))
            .test { cancelAndIgnoreRemainingEvents() }

        coVerify(exactly = 0) { updatePlayerRangeUC.execute(any()) }
        coVerify(exactly = 1) { rangeEnableRangeUC.execute() }
    }

    @Test
    fun `listen range state mapper`() = runTest {
        val event = mockk<RangeBarContract.BarContent>()
        coEvery { rangeStateMapper.flow() } returns flowOf(event)
        tested.processIO(flowOf()).test {
            val item = awaitItem() as? RangeBarContract.Output.BarContentUpdate
            assertEquals(event, item?.barContent)
            cancelAndIgnoreRemainingEvents()
        }
    }

}
