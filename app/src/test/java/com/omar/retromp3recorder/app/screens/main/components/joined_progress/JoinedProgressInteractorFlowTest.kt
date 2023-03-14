package com.omar.retromp3recorder.app.screens.main.components.joined_progress

import app.cash.turbine.test
import com.omar.retromp3recorder.bl.audio.AudioSeekFinishUC
import com.omar.retromp3recorder.bl.audio.AudioSeekPauseUC
import com.omar.retromp3recorder.bl.audio.AudioSeekProgressUC
import com.omar.retromp3recorder.bl.audio.JoinedProgressMapper
import com.omar.retromp3recorder.data.mock.MockExistingFileFactory
import com.omar.retromp3recorder.domain.JoinedProgress
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.utils.platform.Optional
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class JoinedProgressInteractorFlowTest {
    private lateinit var currentFileRepo: CurrentFileRepo
    private val audioSeekProgressUC = mockk<AudioSeekProgressUC>(relaxed = true)
    private val audioSeekPauseUC = mockk<AudioSeekPauseUC>(relaxed = true)
    private val audioSeekFinishUC = mockk<AudioSeekFinishUC>(relaxed = true)
    private val joinedProgressRepo = mockk<JoinedProgressMapper>(relaxed = true)
    private val dispatcher = UnconfinedTestDispatcher()
    private lateinit var tested: JoinedProgressInteractorFlow

    @Before
    fun setUp() {
        currentFileRepo = CurrentFileRepo()
        tested = JoinedProgressInteractorFlow(
            currentFileRepo,
            audioSeekProgressUC,
            audioSeekPauseUC,
            audioSeekFinishUC,
            joinedProgressRepo,
            dispatcher
        )
    }

    @Test
    fun `listen joined progress repo`() = runTest {
        val state = JoinedProgress.Intermediate
        every { joinedProgressRepo.observe() } returns Observable.just(state)
        tested.processIO(flowOf()).test {
            // first event is current file
            skipItems(1)
            val output = awaitItem() as JoinedProgressView.Output.JoinedProgressChanged
            assertEquals(state, output.joinedProgress)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `listen joined currentFileRepo default event`() = runTest {
        val fileWrapper = MockExistingFileFactory.giveFile()
        currentFileRepo.emit(Optional(fileWrapper))
        tested.processIO(flowOf()).test {
            val output = awaitItem() as JoinedProgressView.Output.CurrentFileChanged
            assertEquals(fileWrapper, output.currentFile)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `SeekToPosition input execute seek progress uc`() = runTest {
        val event = JoinedProgressView.In.SeekToPosition(9)
        tested.processIO(flowOf(event)).test { cancelAndIgnoreRemainingEvents() }

        coVerify(exactly = 1) { audioSeekProgressUC.execute(event.position) }
        coVerify(exactly = 0) { audioSeekPauseUC.execute() }
        coVerify(exactly = 0) { audioSeekFinishUC.execute() }
    }

    @Test
    fun `SeekingStarted input execute seek started uc`() = runTest {
        tested.processIO(flowOf(JoinedProgressView.In.SeekingStarted))
            .test { cancelAndIgnoreRemainingEvents() }

        coVerify(exactly = 0) { audioSeekProgressUC.execute(any()) }
        coVerify(exactly = 1) { audioSeekPauseUC.execute() }
        coVerify(exactly = 0) { audioSeekFinishUC.execute() }
    }

    @Test
    fun `SeekingFinished input execute seek finish uc`() = runTest {
        tested.processIO(flowOf(JoinedProgressView.In.SeekingFinished))
            .test { cancelAndIgnoreRemainingEvents() }

        coVerify(exactly = 0) { audioSeekProgressUC.execute(any()) }
        coVerify(exactly = 0) { audioSeekPauseUC.execute() }
        coVerify(exactly = 1) { audioSeekFinishUC.execute() }
    }
}
