package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.data.mock.MockFileFactory
import com.omar.retromp3recorder.storage.db.AppDatabase
import com.omar.retromp3recorder.storage.db.FileDbEntityDao
import com.omar.retromp3recorder.storage.db.toDatabaseEntity
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.utils.platform.FileDeleter
import com.omar.retromp3recorder.utils.domain.Optional
import com.omar.retromp3recorder.utils.domain.repo.first
import com.omar.retromp3recorder.utils.domain.toOptional
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DeleteCurrentFileUCTest {
    private val appDatabase = mockk<AppDatabase>()
    private val dao = mockk<FileDbEntityDao>(relaxed = true)
    private lateinit var currentFileRepo: CurrentFileRepo
    private val fileDeleter = mockk<FileDeleter>(relaxed = true)
    private val takeLastFileUC = mockk<TakeLastFileDbItemUC>()
    private lateinit var tested: DeleteCurrentFileUC

    @Before
    fun setUp() {
        every { appDatabase.fileEntityDao() } returns dao
        every { takeLastFileUC.get() } returns Single.just(Optional.empty())
        currentFileRepo = CurrentFileRepo()
        tested = DeleteCurrentFileUC(
            appDatabase, currentFileRepo, fileDeleter, takeLastFileUC
        )
    }

    @Test
    fun `WHEN current file null not executed`() = runTest {
        val file1 = MockFileFactory.giveExistingFile()

        currentFileRepo.emit(Optional.empty())

        every { takeLastFileUC.get() } returns Single.just(file1.toOptional())

        tested.execute()

        verify(exactly = 0) {
            fileDeleter.deleteFile(any())
        }
        verify(exactly = 0) {
            dao.delete(any())
        }
        verify(exactly = 0) {
            takeLastFileUC.get()
        }

        val currentFile = currentFileRepo.first().value
        assertNotEquals(currentFile, file1)
    }

    @Test
    fun `WHEN current file exists executed in order`() = runTest {
        val file = MockFileFactory.giveExistingFile()
        val file1 = MockFileFactory.giveExistingFile()

        currentFileRepo.emit(file.toOptional())

        every { takeLastFileUC.get() } returns Single.just(file1.toOptional())

        tested.execute()

        verifyOrder {
            fileDeleter.deleteFile(file.path)
            dao.delete(listOf(file.toDatabaseEntity()))
            takeLastFileUC.get()
        }

        val currentFile = currentFileRepo.first().value
        assertEquals(currentFile, file1)
    }

    @Test
    fun `WHEN current file FUTURE not executed`() = runTest {
        val file = MockFileFactory.giveFutureFile()
        val file1 = MockFileFactory.giveExistingFile()

        currentFileRepo.emit(file.toOptional())

        every { takeLastFileUC.get() } returns Single.just(file1.toOptional())

        tested.execute()

        verify(exactly = 0) {
            fileDeleter.deleteFile(any())
        }
        verify(exactly = 0) {
            dao.delete(any())
        }
        verify(exactly = 0) {
            takeLastFileUC.get()
        }

        val currentFile = currentFileRepo.first().value
        assertNotEquals(currentFile, file1)
    }
}
