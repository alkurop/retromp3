package com.omar.retromp3recorder.app.screens.home.components.joined_progress

import app.cash.turbine.test
import com.omar.retromp3recorder.bl.audio.progress.AudioSeekFinishUC
import com.omar.retromp3recorder.bl.audio.progress.AudioSeekPauseUC
import com.omar.retromp3recorder.bl.audio.progress.AudioSeekProgressUC
import com.omar.retromp3recorder.bl.audio.progress.JoinedProgressMapper
import com.omar.retromp3recorder.data.mock.MockFileFactory
import com.omar.retromp3recorder.domain.JoinedProgress
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.utils.domain.DifferedCoroutineScope
import com.omar.retromp3recorder.utils.domain.Optional
import io.mockk.coVerify
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
class JoinedProgressInteractorTest {
    private lateinit var currentFileRepo: CurrentFileRepo
    private val audioSeekProgressUC = mockk<AudioSeekProgressUC>(relaxed = true)
    private val audioSeekPauseUC = mockk<AudioSeekPauseUC>(relaxed = true)
    private val audioSeekFinishUC = mockk<AudioSeekFinishUC>(relaxed = true)
    private val joinedProgressRepo = mockk<JoinedProgressMapper>(relaxed = true)
    private val dispatcher = UnconfinedTestDispatcher()
    private lateinit var tested: JoinedProgressInteractor

    @Before
    fun setUp() {
        currentFileRepo = CurrentFileRepo()
        tested = JoinedProgressInteractor(
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
        every { joinedProgressRepo.flow() } returns flowOf(state)
        tested.processIO(DifferedCoroutineScope(dispatcher), flowOf()).test {
            val awaitItem = awaitItem()
            println(awaitItem)
            val output = awaitItem as JoinedProgressContract.Output.JoinedProgressChanged
            assertEquals(state, output.joinedProgress)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `listen joined currentFileRepo default event`() = runTest {
        val fileWrapper = MockFileFactory.giveExistingFile()
        currentFileRepo.emit(Optional(fileWrapper))
        tested.processIO(DifferedCoroutineScope(dispatcher), flowOf()).test {
            val output = awaitItem() as JoinedProgressContract.Output.CurrentFileChanged
            assertEquals(fileWrapper, output.currentFile)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `SeekToPosition input execute seek progress uc`() = runTest {
        val event = JoinedProgressContract.In.SeekToPosition(9)
        tested.processIO(DifferedCoroutineScope(dispatcher), flowOf(event))
            .test { cancelAndIgnoreRemainingEvents() }

        coVerify(exactly = 1) { audioSeekProgressUC.execute(event.position) }
        coVerify(exactly = 0) { audioSeekPauseUC.execute() }
        coVerify(exactly = 0) { audioSeekFinishUC.execute() }
    }

    @Test
    fun `SeekingStarted input execute seek started uc`() = runTest {
        tested.processIO(
            DifferedCoroutineScope(dispatcher),
            flowOf(JoinedProgressContract.In.SeekingStarted)
        )
            .test { cancelAndIgnoreRemainingEvents() }

        coVerify(exactly = 0) { audioSeekProgressUC.execute(any()) }
        coVerify(exactly = 1) { audioSeekPauseUC.execute() }
        coVerify(exactly = 0) { audioSeekFinishUC.execute() }
    }

    @Test
    fun `SeekingFinished input execute seek finish uc`() = runTest {
        tested.processIO(
            DifferedCoroutineScope(dispatcher),
            flowOf(JoinedProgressContract.In.SeekingFinished)
        )
            .test { cancelAndIgnoreRemainingEvents() }

        coVerify(exactly = 0) { audioSeekProgressUC.execute(any()) }
        coVerify(exactly = 0) { audioSeekPauseUC.execute() }
        coVerify(exactly = 1) { audioSeekFinishUC.execute() }
    }
}
