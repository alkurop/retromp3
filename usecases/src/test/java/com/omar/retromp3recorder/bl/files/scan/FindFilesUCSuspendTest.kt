package com.omar.retromp3recorder.bl.files.scan

import com.omar.retromp3recorder.data.mock.MockFileFactory
import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.utils.domain.ScopeJobWrapper
import com.omar.retromp3recorder.utils.platform.DirPathProvider
import com.omar.retromp3recorder.utils.platform.FileEmptyChecker
import com.omar.retromp3recorder.utils.platform.FileLister
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@Suppress("UNUSED")
@OptIn(ExperimentalCoroutinesApi::class)
class FindFilesUCSuspendTest {

    @MockK
    private lateinit var fileEmptyChecker: FileEmptyChecker

    @MockK
    private lateinit var dirPathProvider: DirPathProvider

    @MockK(relaxed = true)
    private lateinit var fileLister: FileLister

    private val scopeJobWrapper = ScopeJobWrapper(UnconfinedTestDispatcher())

    @InjectMockKs
    private lateinit var tested: FindFilesUCSuspend

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        every { dirPathProvider.fileDirs } returns listOf("dir")
    }

    @Test
    fun `empty files are filtered out`() = runTest {
        val list = (1..10)
            .map { MockFileFactory.giveExistingFile().copy(createTimedStamp = it.toLong()) }
        every { fileLister.listAudioFiles(any()) } returns list.reversed()
        every { fileEmptyChecker.isFileEmpty(any()) } returns true

        val result = tested.execute()
        assertEquals(emptyList<ExistingFileWrapper>(), result)
    }

    @Test
    fun `files are sorted ACS by created timestamp`() = runTest {
        val list = (1..10)
            .map { MockFileFactory.giveExistingFile().copy(createTimedStamp = it.toLong()) }
        every { fileLister.listAudioFiles(any()) } returns list.reversed()
        every { fileEmptyChecker.isFileEmpty(any()) } returns false

        val result = tested.execute()
        assertEquals(list, result)
    }
}
