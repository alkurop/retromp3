package com.omar.retromp3recorder.bl.audio.record

import com.omar.retromp3recorder.data.mock.MockFileFactory
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.utils.platform.toOptional
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RecordWavetableUCSuspendTest {
    private val collectWavetableUC = mockk<CollectWavetableUC>()
    private val saveRecordingWithWavetableUC =
        mockk<SaveRecordingWithWavetableUCSuspend>(relaxed = true)
    private lateinit var currentFileRepo: CurrentFileRepo
    private val dispatcher = UnconfinedTestDispatcher()

    lateinit var tested: RecordWavetableUCSuspend

    @Before
    fun setUp() {
        currentFileRepo = CurrentFileRepo()
        tested = RecordWavetableUCSuspend(
            saveRecordingWithWavetableUC,
            collectWavetableUC,
            currentFileRepo,
            dispatcher
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun `when current file null then crashes`() = runTest {
        coEvery { collectWavetableUC.execute() } returns mockk()

        tested.execute()
    }

    @Test(expected = IllegalArgumentException::class)
    fun `when current file existing then crashes`() = runTest {
        currentFileRepo.emit(MockFileFactory.giveExistingFile().toOptional())
        coEvery { collectWavetableUC.execute() } returns mockk()

        tested.execute()
    }

    @Test
    fun `when current file future then executes`() = runTest {
        currentFileRepo.emit(MockFileFactory.giveFutureFile().toOptional())
        coEvery { collectWavetableUC.execute() } returns mockk()

        tested.execute()
    }
}
