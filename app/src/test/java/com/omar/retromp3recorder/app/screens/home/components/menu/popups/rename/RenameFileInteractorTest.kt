package com.omar.retromp3recorder.app.screens.home.components.menu.popups.rename

import app.cash.turbine.test
import com.omar.retromp3recorder.bl.files.CanRenameNameUC
import com.omar.retromp3recorder.bl.files.RenameFileUC
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
class RenameFileInteractorTest {
    private val canRenameNameUC = mockk<CanRenameNameUC>(relaxed = true)
    private lateinit var currentFileRepo: CurrentFileRepo
    private val renameFileUC = mockk<RenameFileUC>(relaxed = true)
    private val dispatcher = UnconfinedTestDispatcher()
    private lateinit var tested: RenameFileInteractor

    @Before
    fun setUp() {
        currentFileRepo = CurrentFileRepo()
        tested =
            RenameFileInteractor(canRenameNameUC, currentFileRepo, renameFileUC, dispatcher)
    }

    @Test
    fun `when current file is not existing the crash`() = runTest {
        currentFileRepo.emit(MockFileFactory.giveFutureFile().toOptional())
        tested.processIO(flowOf())
            .test {
                val error = awaitError()
                assert(error is IllegalArgumentException)
            }
    }

    @Test
    fun `can rename input triggers usecase and emits result`() = runTest {
        currentFileRepo.emit(MockFileFactory.giveExistingFile().toOptional())
        val newName = "test"
        tested.processIO(
            flowOf(RenameFileContract.Input.CheckCanRename(newName))
        )
            .test {
                // skip file event
                skipItems(1)
                val item = awaitItem()
                assert(item is RenameFileContract.Output.OkButtonState)
            }

        coVerify { canRenameNameUC.execute(newName) }
    }

    @Test
    fun `rename input triggers usecase and emits dismiss`() = runTest {
        currentFileRepo.emit(MockFileFactory.giveExistingFile().toOptional())
        val newName = "test"
        tested.processIO(
            flowOf(RenameFileContract.Input.Rename(newName))
        ).test {
            // skip file event
            skipItems(1)
            val item = awaitItem()
            assert(item is RenameFileContract.Output.Dismiss)
        }

        coVerify { renameFileUC.execute(newName) }
    }

    @Test
    fun `listens to current file repo`() = runTest {
        val expected = MockFileFactory.giveExistingFile()
        currentFileRepo.emit(expected.toOptional())
        tested.processIO(flowOf())
            .test {
                val result = (awaitItem() as? RenameFileContract.Output.CurrentFile)?.fileWrapper
                assertEquals(result, expected)
            }

    }
}
