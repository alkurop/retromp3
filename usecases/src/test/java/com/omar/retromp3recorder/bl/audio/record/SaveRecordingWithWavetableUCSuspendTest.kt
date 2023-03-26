package com.omar.retromp3recorder.bl.audio.record

import com.omar.retromp3recorder.data.mock.MockFileFactory
import com.omar.retromp3recorder.domain.Wavetable
import com.omar.retromp3recorder.storage.db.AppDatabase
import com.omar.retromp3recorder.storage.db.FileDbEntityDao
import com.omar.retromp3recorder.storage.db.toDatabaseEntity
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.utils.domain.repo.first
import com.omar.retromp3recorder.utils.platform.FileLister
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SaveRecordingWithWavetableUCSuspendTest {

    private val appDatabase = mockk<AppDatabase>()
    private val dao = mockk<FileDbEntityDao>(relaxed = true)
    private val fileLister = mockk<FileLister>(relaxed = true)
    private val saveMp3TagsUC = mockk<SaveMp3TagsUCSuspend>(relaxed = true)
    private lateinit var currentFileRepo: CurrentFileRepo
    private val dispatcher = UnconfinedTestDispatcher()

    private lateinit var tested: SaveRecordingWithWavetableUCSuspend

    @Before
    fun setUp() {
        currentFileRepo = CurrentFileRepo()
        every { appDatabase.fileEntityDao() } returns dao
        tested = SaveRecordingWithWavetableUCSuspend(
            appDatabase,
            fileLister,
            saveMp3TagsUC,
            currentFileRepo,
            dispatcher
        )
    }

    @Test
    fun `execute sequence executed`() = runTest {
        val wavetable = Wavetable(ByteArray(10), 100)
        val path = "path"
        val data = path to wavetable

        val file = MockFileFactory.giveExistingFile()
        every { fileLister.discoverFile(path) } returns file
        every { fileLister.discoverLength(path) } returns 1
        every { dao.insert(any()) } returns 1

        tested.execute(data)

        coVerify {
            saveMp3TagsUC.execute(path)
            fileLister.discoverFile(path)
            fileLister.discoverLength(path)
            dao.insert(file.copy(length = 1, wavetable = wavetable).toDatabaseEntity())
        }
    }

    @Test
    fun `on execute current file updated`() = runTest {
        val wavetable = Wavetable(ByteArray(10), 100)
        val path = "path"
        val data = path to wavetable

        val file = MockFileFactory.giveExistingFile()
        every { fileLister.discoverFile(path) } returns file
        every { fileLister.discoverLength(path) } returns 1
        every { dao.insert(any()) } returns 1

        tested.execute(data)
        val expected = file.copy(length = 1, id = 1, wavetable = wavetable)

        assertEquals(expected, currentFileRepo.first().value)
    }

}
