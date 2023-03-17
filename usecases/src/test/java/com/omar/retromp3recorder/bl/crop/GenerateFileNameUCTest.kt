package com.omar.retromp3recorder.bl.crop

import com.omar.retromp3recorder.bl.files.GetCropFileNameUC
import com.omar.retromp3recorder.data.mock.MockFileFactory
import com.omar.retromp3recorder.data.mock.MockSuggestionFactory
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.utils.platform.toOptional
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GenerateFileNameUCTest {
    private lateinit var currentFileRepo: CurrentFileRepo
    private val cropGeneratorUC = mockk<GetCropFileNameUC>()
    private lateinit var tested: GenerateFileNameUC
    private val nameSuggestion = MockSuggestionFactory.giveTestSuggestion()

    @Before
    fun setUp() {
        currentFileRepo = CurrentFileRepo()
        tested = GenerateFileNameUC(currentFileRepo, cropGeneratorUC)

        coEvery { cropGeneratorUC.execute(any()) } returns  nameSuggestion
    }

    @Test(expected = java.lang.IllegalArgumentException::class)
    fun `WHEN current file empty THEN crash`() = runTest {
        tested.execute()
    }

    @Test(expected = java.lang.IllegalArgumentException::class)
    fun `WHEN current file not current THEN crash`() = runTest {
        currentFileRepo.emit(MockFileFactory.giveFutureFile().toOptional())
        tested.execute()
    }

    @Test
    fun `WHEN current file existing THEN generate file`() = runTest {
        currentFileRepo.emit(MockFileFactory.giveExistingFile().toOptional())
        val result = tested.execute()
        assertEquals(nameSuggestion, result)
    }
}
