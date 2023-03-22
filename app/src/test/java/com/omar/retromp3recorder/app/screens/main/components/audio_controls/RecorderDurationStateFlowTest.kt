package com.omar.retromp3recorder.app.screens.main.components.audio_controls

import app.cash.turbine.test
import com.omar.retromp3recorder.bl.audio.progress.JoinedProgressMapper
import com.omar.retromp3recorder.domain.JoinedProgress
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RecorderDurationStateFlowTest {

    private val mapper: JoinedProgressMapper = mockk()
    private lateinit var tested: RecorderDurationStateFlow


    @Before
    fun setUp() {
        tested = RecorderDurationStateFlow(mapper)
    }

    @Test
    fun `on state Hidden output with progress null`() = runTest {
        every { mapper.flow() } returns flowOf(JoinedProgress.Hidden)

        tested.flow().test {
            val item = awaitItem()
            assertEquals(null, item.duration)
            awaitComplete()
        }
    }
    @Test
    fun `on state Intermediate output with progress null`() = runTest {
        every { mapper.flow() } returns flowOf(JoinedProgress.Intermediate)

        tested.flow().test {
            val item = awaitItem()
            assertEquals(null, item.duration)
            awaitComplete()
        }
    }
    @Test
    fun `on state PlayerProgressShown output with progress null`() = runTest {
        val state = mockk< JoinedProgress.PlayerProgressShown >()
        every { mapper.flow() } returns flowOf(state)

        tested.flow().test {
            val item = awaitItem()
            assertEquals(null, item.duration)
            awaitComplete()
        }
    }
    @Test
    fun `on state RecorderProgressShown output with progress exists`() = runTest {
        val expectedDuration: Long = 99

        val state = JoinedProgress.RecorderProgressShown(expectedDuration, mockk())

        every { mapper.flow() } returns flowOf(state)

        tested.flow().test {
            val item = awaitItem()
            assertEquals(expectedDuration, item.duration)
            awaitComplete()
        }
    }

}
