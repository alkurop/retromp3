package com.omar.retromp3recorder.app.screens.home.components.log

import app.cash.turbine.test
import com.omar.retromp3recorder.domain.platform.LogEvent
import com.omar.retromp3recorder.bl.system.LogMapper
import com.omar.retromp3recorder.utils.domain.DifferedCoroutineScope
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LogInteractorTest {
    private val dispatcher = UnconfinedTestDispatcher()
    private val mapper = mockk<LogMapper>()
    private lateinit var tested: LogInteractor

    @Before
    fun setUp() {
        tested = LogInteractor(mapper, dispatcher)
    }

    @Test
    fun `listen log mapper error events SENDS error output`() = runTest {
        every { mapper.flow() } returns flowOf(LogEvent.Error(mockk()))
        tested.processIO( ).test {
            val item = awaitItem()
            awaitComplete()
            assert(item is LogView.Output.ErrorLogOutput)
        }
    }

    @Test
    fun `listen log mapper message events SENDS message output`() = runTest {
        every { mapper.flow() } returns flowOf(LogEvent.Message(mockk()))
        tested.processIO( ).test {
            val item = awaitItem()
            awaitComplete()
            assert(item is LogView.Output.MessageLogOutput)
        }
    }
}
