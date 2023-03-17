package com.omar.retromp3recorder.bl.system

import app.cash.turbine.test
import com.omar.retromp3recorder.domain.platform.LogEvent
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.share.Sharer
import com.omar.retromp3recorder.storage.repo.global.LogRepo
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LogMapperFlowTest {
    private val recorder = mockk<Mp3VoiceRecorder>()
    private val sharer = mockk<Sharer>()

    private lateinit var logRepo: LogRepo
    private lateinit var tested: LogMapperFlow


    @Before
    fun setUp() {
        every { recorder.observeEvents() } returns Observable.never()
        every { sharer.observeEvents() } returns Observable.never()
        logRepo = LogRepo()
        tested = LogMapperFlow(recorder, sharer, logRepo)
    }

    @Test
    fun `recorder error sends error`() = runTest {
        every { recorder.observeEvents() } returns Observable.just(
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
        every { recorder.observeEvents() } returns Observable.just(
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
        every { sharer.observeEvents() } returns Observable.just(
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
        every { sharer.observeEvents() } returns Observable.just(
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
