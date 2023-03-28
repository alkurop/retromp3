package com.omar.retromp3recorder.bl.files.scan

import com.omar.retromp3recorder.bl.database.GetPagingItemsDatabaseUCFlow
import com.omar.retromp3recorder.bl.system.WaveformScanUpdaterUCSuspend
import com.omar.retromp3recorder.data.mock.MockFileFactory
import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.storage.db.AppDatabase
import com.omar.retromp3recorder.storage.db.FileDbEntity
import com.omar.retromp3recorder.storage.db.FileDbEntityDao
import com.omar.retromp3recorder.storage.db.toDatabaseEntity
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.utils.domain.ScopeJobWrapper
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@Suppress("UNUSED", "UNCHECKED_CAST")
@OptIn(ExperimentalCoroutinesApi::class)
class ScanDirFilesPartialUCSuspendTest {
    @MockK
    private lateinit var appDatabase: AppDatabase

    @MockK(relaxed = true)
    private lateinit var dao: FileDbEntityDao

    @MockK
    private lateinit var findFilesUC: FindFilesUCSuspend
    private lateinit var currentFileRepo: CurrentFileRepo

    @MockK
    private lateinit var getPagingItemsDatabaseUC: GetPagingItemsDatabaseUCFlow

    @MockK
    private lateinit var waveformScanUpdaterUC: WaveformScanUpdaterUCSuspend

    @MockK
    private lateinit var collector: FileUpdatePayloadCollectorUC

    private var jobWrapper = ScopeJobWrapper(UnconfinedTestDispatcher())

    @InjectMockKs
    private lateinit var tested: ScanDirFilesPartialUCSuspend

    @Before
    fun setUp() {
        currentFileRepo = CurrentFileRepo()
        MockKAnnotations.init(this)
        every { appDatabase.fileEntityDao() } returns dao
    }

    @Test
    fun `deletes are deleted,updates are updated, inserts are inserted`() = runTest {
        val updateFiles = (1..10).map { MockFileFactory.giveExistingFile() }
        val insertFiles = (1..10).map { MockFileFactory.giveExistingFile() }
        val otherFiles = (1..10).map { MockFileFactory.giveExistingFile() }
        val deletes = (1..10).map { MockFileFactory.giveExistingFile() }

        coEvery { findFilesUC.execute() } returns updateFiles + insertFiles + otherFiles
        coEvery { waveformScanUpdaterUC.execute(any()) } answers {
            val input = it.invocation.args[0] as List<ExistingFileWrapper>
            input
        }
        every { dao.insertBatch(any()) } answers {
            (invocation.args[0] as List<FileDbEntity>).map { it.id }
        }
        every { getPagingItemsDatabaseUC.flow(any()) } returns flowOf((otherFiles + updateFiles).map { it.toDatabaseEntity() })
        every {
            collector.execute(
                any(),
                any()
            )
        } returns DbBatchUpdatePayload(
            deletes = deletes.map { it.toDatabaseEntity() },
            inserts = insertFiles.map { it.toDatabaseEntity() },
            updates = updateFiles.map { it.toDatabaseEntity() },
        )

        tested.execute()
        coVerifySequence {
            dao.delete(deletes.map { it.toDatabaseEntity() })
            dao.update(updateFiles.map { it.toDatabaseEntity() })
            dao.insertBatch(insertFiles.map { it.toDatabaseEntity() })
            waveformScanUpdaterUC.execute(insertFiles + updateFiles)
        }

        val currentFile = currentFileRepo.first().value
        assertEquals(currentFile, insertFiles.first())
    }

}
