package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.bl.files.scan.ScanDirFilesPartialUCSuspend
import com.omar.retromp3recorder.data.mock.MockFileFactory
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.utils.domain.ScopeJobWrapper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TakeLastFileDirScanUCSuspendTest {
    private lateinit var currentFileRepo: CurrentFileRepo
    private val scanDirFilesPartialUC: ScanDirFilesPartialUCSuspend = mockk()
    private val takeLastFileFastUC: TakeLastFileDbItemUCSuspend = mockk()
    private val dispatcher = UnconfinedTestDispatcher()
    private val scopeJobWrapper = ScopeJobWrapper(dispatcher)
    private lateinit var tested: TakeLastFileDirScanUCSuspend

    @Before
    fun setUp() {
        currentFileRepo = CurrentFileRepo()
        tested = TakeLastFileDirScanUCSuspend(
            currentFileRepo,
            scanDirFilesPartialUC,
            takeLastFileFastUC,
            scopeJobWrapper
        )
    }

    @Test
    fun `execute calls scan partial usecase`() = runTest {
            // Given
            coEvery { takeLastFileFastUC.execute() } returns null
            // When
            tested.execute()
            // Then
            coVerify { scanDirFilesPartialUC.execute() }
        }

    @Test
    fun `when takeLastFileFastUC returns a database file execute emit to current file repo`() =
        runTest {
            // Given
            val file = MockFileFactory.giveExistingFile()
            coEvery { takeLastFileFastUC.execute() } returns file
            // When
            tested.execute()
            // Then
            val result = currentFileRepo.first().value
            assertEquals(result, file)
        }
}
