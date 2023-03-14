package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.utils.domain.FileRenamer
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CanSaveAsNameTest {
    private val fileRenamer = mockk<FileRenamer>()
    private lateinit var tested: CanSaveAsNameUC

    @Before
    fun setUp() {
        tested = CanSaveAsNameUC(fileRenamer)
    }

    @Test
    fun `WHEN file renamer returns true Result false`() = runTest {
        every { fileRenamer.exists(any()) } returns true
        assertFalse(tested.execute(""))
    }

    @Test
    fun `WHEN file renamer returns false Result true`() = runTest {
        every { fileRenamer.exists(any()) } returns false
        assert(tested.execute(""))
    }
}
