package com.omar.retromp3recorder.app.screens.main.components.audio_controls

import app.cash.turbine.test
import com.omar.retromp3recorder.bl.audio.JoinedProgressMapper
import com.omar.retromp3recorder.domain.JoinedProgress
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
        every { mapper.observe() } returns Observable.just(JoinedProgress.Hidden)

        tested.flow().test {
            val item = awaitItem()
            assertEquals(null, item.duration)
            awaitComplete()
        }
    }
    @Test
    fun `on state Intermediate output with progress null`() = runTest {
        every { mapper.observe() } returns Observable.just(JoinedProgress.Intermediate)

        tested.flow().test {
            val item = awaitItem()
            assertEquals(null, item.duration)
            awaitComplete()
        }
    }
    @Test
    fun `on state PlayerProgressShown output with progress null`() = runTest {
        val state = mockk< JoinedProgress.PlayerProgressShown >()
        every { mapper.observe() } returns Observable.just(state)

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

        every { mapper.observe() } returns Observable.just(state)

        tested.flow().test {
            val item = awaitItem()
            assertEquals(expectedDuration, item.duration)
            awaitComplete()
        }
    }

}
