package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.data.mock.MockFileFactory
import com.omar.retromp3recorder.storage.db.AppDatabase
import com.omar.retromp3recorder.storage.db.FileDbEntityDao
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.utils.platform.FileRenamer
import com.omar.retromp3recorder.utils.platform.Mp3TagsEditor
import com.omar.retromp3recorder.utils.domain.Optional
import com.omar.retromp3recorder.utils.domain.repo.first
import com.omar.retromp3recorder.utils.domain.toOptional
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RenameFileUCTest {
    private val appDatabase = mockk<AppDatabase>()
    private val dao = mockk<FileDbEntityDao>(relaxed = true)
    private lateinit var currentFileRepo: CurrentFileRepo
    private val fileRenamer = mockk<FileRenamer>(relaxed = true)
    private val mp3TagsEditor = mockk<Mp3TagsEditor>(relaxed = true)
    private lateinit var tested: RenameFileUC

    @Before
    fun setUp() {
        every { appDatabase.fileEntityDao() } returns dao
        currentFileRepo = CurrentFileRepo()
        tested = RenameFileUC(
            appDatabase, currentFileRepo, fileRenamer, mp3TagsEditor
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun `when file not then crash`() = runTest {
        currentFileRepo.emit(Optional.empty())
        tested.execute("test")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `when file not existing then crash`() = runTest {
        currentFileRepo.emit(MockFileFactory.giveFutureFile().toOptional())
        tested.execute("test")
    }

    @Test
    fun `happy path execute sequence`() = runTest {
        val existingFile = MockFileFactory.giveExistingFile()
        currentFileRepo.emit(existingFile.toOptional())
        val newName = "newName"

        tested.execute(newName)
        verifyOrder {
            fileRenamer.renameFile(existingFile, newName)
            mp3TagsEditor.getTags(any())
            mp3TagsEditor.setTags(any(), any())
            dao.updateItem(any())
        }
        val newCurrentFile  =currentFileRepo.first().value
        assertNotEquals(existingFile, newCurrentFile)

    }
}
