package com.omar.retromp3recorder.app.screens.home.components.menu.visibility_logic

import app.cash.turbine.test
import com.omar.retromp3recorder.app.screens.home.components.menu.MenuContract
import com.omar.retromp3recorder.bl.enablers.EnablersSwitcher
import com.omar.retromp3recorder.domain.AudioEnabler
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
class MenuInteractorFlowTest {

    private val excavator = mockk<MenuStateExcavatorFlow>(relaxed = true)
    private val enabler = mockk<EnablersSwitcher>(relaxed = true)
    private val dispatcher = UnconfinedTestDispatcher()

    private lateinit var tested: MenuInteractorFlow

    @Before
    fun setUp() {
        tested = MenuInteractorFlow(excavator, enabler, dispatcher)
    }

    @Test
    fun `Enable input executes switcher usecase`() = runTest {
        val event = MenuContract.Input.Enable(AudioEnabler.Reverse, true)
        tested.processIO(flowOf(event)).test {
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { enabler.execute(event.enabler, event.isEnabled) }
    }

    @Test
    fun `listens to menu excavator`() = runTest {
        val state = mockk<MenuContract.State>()
        coEvery { excavator.flow() } returns flowOf(state)
        tested.processIO(flowOf()).test {
            val item = awaitItem()
            assertEquals(state, item)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
