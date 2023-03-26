package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.data.mock.MockFileFactory
import com.omar.retromp3recorder.domain.FutureFileWrapper
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.utils.platform.FileRenamer
import com.omar.retromp3recorder.utils.domain.toOptional
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CanRenameNameTest {
    private lateinit var currentFileRepo: CurrentFileRepo
    private val fileRenamer = mockk<FileRenamer>()
    private lateinit var tested: CanRenameNameUC

    @Before
    fun setUp() {
        currentFileRepo = CurrentFileRepo()
        tested = CanRenameNameUC(currentFileRepo, fileRenamer)
    }

    @Test
    fun `WHEN filepath empty THEN can rename false`() = runTest {
        assertFalse(tested.execute(""))
    }

    @Test
    fun `WHEN current file null THEN can rename false`() = runTest {
        assertFalse(tested.execute("path"))
    }

    @Test
    fun `WHEN current file not Existing THEN can rename false`() = runTest {
        currentFileRepo.emit(FutureFileWrapper("path").toOptional())

        assertFalse(tested.execute("path"))
    }

    @Test
    fun `WHEN file renamer return False then can rename false`() = runTest {
        currentFileRepo.emit(MockFileFactory.giveExistingFile().toOptional())

        every { fileRenamer.canRename(any(), any()) } returns false

        assertFalse(tested.execute("path"))
    }

    @Test
    fun `WHEN file renamer return true then can rename true`() = runTest {
        currentFileRepo.emit(MockFileFactory.giveExistingFile().toOptional())

        every { fileRenamer.canRename(any(), any()) } returns true

        assert(tested.execute("path"))
    }
}

