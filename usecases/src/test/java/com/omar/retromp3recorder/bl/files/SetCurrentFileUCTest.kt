package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.data.mock.MockFileFactory
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.utils.domain.repo.first
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SetCurrentFileUCTest {

    private lateinit var currentFileRepo: CurrentFileRepo
    private lateinit var tested: SetCurrentFileUC

    @Before
    fun setUp() {
        currentFileRepo = CurrentFileRepo()
        tested = SetCurrentFileUC(currentFileRepo)
    }

    @Test
    fun `new existing file was set`() = runTest {
        val file = MockFileFactory.giveExistingFile()
        tested.execute(file)

        assertEquals(file, currentFileRepo.first().value)
    }

}
