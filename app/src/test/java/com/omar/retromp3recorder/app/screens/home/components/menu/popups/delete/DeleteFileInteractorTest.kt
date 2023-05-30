package com.omar.retromp3recorder.app.screens.home.components.menu.popups.delete

import app.cash.turbine.test
import com.omar.retromp3recorder.bl.files.DeleteCurrentFileUC
import com.omar.retromp3recorder.data.mock.MockFileFactory
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.utils.domain.toOptional
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
class DeleteFileInteractorTest {

    private val deleteCurrentFileUC = mockk<DeleteCurrentFileUC>(relaxed = true)
    private lateinit var currentFileRepo: CurrentFileRepo
    private val dispatcher = UnconfinedTestDispatcher()
    private lateinit var tested: DeleteFileInteractor

    @Before
    fun setUp() {
        currentFileRepo = CurrentFileRepo()
        tested = DeleteFileInteractor(deleteCurrentFileUC, currentFileRepo, dispatcher)
    }

    @Test
    fun `delete file executes usecase`() = runTest {
        tested.processIO(flowOf(DeleteFileContract.Input.DeleteFile)).test {
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { deleteCurrentFileUC.execute() }
    }


    @Test
    fun `listens to current file repo`() = runTest {
        val file = MockFileFactory.giveExistingFile()
        currentFileRepo.emit(file.toOptional())
        tested.processIO(flowOf(DeleteFileContract.Input.DeleteFile)).test {
            val resultFile = (awaitItem() as? DeleteFileContract.Output.CurrentFile)?.fileWrapper
            assertEquals(file, resultFile)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `delete file executes dismiss dialog`() = runTest {
        tested.processIO(flowOf(DeleteFileContract.Input.DeleteFile)).test {
            //skip initial file event
            skipItems(1)
            val item = awaitItem()
            assertEquals(item, DeleteFileContract.Output.Dismiss)
        }
    }

}
