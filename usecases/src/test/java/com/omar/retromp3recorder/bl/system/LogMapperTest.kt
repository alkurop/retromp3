package com.omar.retromp3recorder.bl.system

import app.cash.turbine.test
import com.omar.retromp3recorder.domain.platform.LogEvent
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.share.Sharer
import com.omar.retromp3recorder.storage.repo.global.LogRepo
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LogMapperTest {
    private val recorder = mockk<Mp3VoiceRecorder>()
    private val sharer = mockk<Sharer>()

    private lateinit var logRepo: LogRepo
    private lateinit var tested: LogMapper


    @Before
    fun setUp() {
        every { recorder.eventsFlow() } returns flowOf()
        every { sharer.flow() } returns flowOf()
        logRepo = LogRepo()
        tested = LogMapper(recorder, sharer, logRepo)
    }

    @Test
    fun `recorder error sends error`() = runTest {
        every { recorder.eventsFlow() } returns flowOf(
            Mp3VoiceRecorder.Event.Error(
                mockk()
            )
        )
        tested.flow().test {
            val event = awaitItem()
            assert(event is LogEvent.Error)
        }
    }

    @Test
    fun `recorder message sends message`() = runTest {
        every { recorder.eventsFlow() } returns flowOf(
            Mp3VoiceRecorder.Event.Message(
                mockk()
            )
        )
        tested.flow().test {
            val event = awaitItem()
            assert(event is LogEvent.Message)
        }
    }

    @Test
    fun `sharer message sends message`() = runTest {
        every { sharer.flow() } returns flowOf(
            Sharer.Event.SharingOk(
                mockk()
            )
        )
        tested.flow().test {
            val event = awaitItem()
            assert(event is LogEvent.Message)
        }
    }

    @Test
    fun `sharer error sends error`() = runTest {
        every { sharer.flow() } returns flowOf(
            Sharer.Event.Error(
                mockk()
            )
        )
        tested.flow().test {
            val event = awaitItem()
            assert(event is LogEvent.Error)
        }
    }

    @Test
    fun `logRepo error sends error`() = runTest {
        logRepo.emit(LogEvent.Error(mockk()))
        tested.flow().test {
            val event = awaitItem()
            assert(event is LogEvent.Error)
        }
    }

    @Test
    fun `logRepo message sends message`() = runTest {
        logRepo.emit(LogEvent.Message(mockk()))

        tested.flow().test {
            val event = awaitItem()
            assert(event is LogEvent.Message)
        }
    }
}
