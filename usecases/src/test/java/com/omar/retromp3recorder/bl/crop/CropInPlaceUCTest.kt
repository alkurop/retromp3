package com.omar.retromp3recorder.bl.crop

import com.omar.retromp3recorder.bl.actions.CropWithProductUC
import com.omar.retromp3recorder.data.mock.MockFileFactory
import com.omar.retromp3recorder.data.mock.MockSuggestionFactory
import com.omar.retromp3recorder.domain.toResult
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CropInPlaceUCTest {
    private lateinit var currentFileRepo: CurrentFileRepo
    private val cropOutsideUC = mockk<CropWithProductUC>()
    private lateinit var tested: CropInPlaceUC

    private val suggestion = MockSuggestionFactory.giveTestSuggestion()

    @Before
    fun setUp() {
        currentFileRepo = CurrentFileRepo()
        tested = CropInPlaceUC(cropOutsideUC, currentFileRepo)
    }

    @Test
    fun `when crop outside failed THEN crop repo NOT updated`() = runTest {
        coEvery { cropOutsideUC.execute(any()) } returns Error("expected").toResult()
        tested.execute(suggestion)

        assertNull(currentFileRepo.first().value)
    }

    @Test
    fun `when crop outside success THEN crop repo IS updated`() = runTest {
        val file = MockFileFactory.giveExistingFile()

        coEvery { cropOutsideUC.execute(any()) } returns file.toResult()
        tested.execute(suggestion)

        val result = currentFileRepo.first().value
        assertEquals(file, result)
    }
}
