package com.omar.retromp3recorder.bl.crop

import app.cash.turbine.test
import com.omar.retromp3recorder.bl.files.GetCropFileNameUC
import com.omar.retromp3recorder.data.mock.MockFileFactory
import com.omar.retromp3recorder.data.mock.MockSuggestionFactory
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.utils.domain.toOptional
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CropFileNameUpdaterTest {
    private lateinit var currentFileRepo: CurrentFileRepo
    private val cropGeneratorUC = mockk<GetCropFileNameUC>()
    private lateinit var tested: CropFileNameUpdater

    @Before
    fun setUp() {
        currentFileRepo = CurrentFileRepo()
        tested = CropFileNameUpdater(currentFileRepo, cropGeneratorUC)
    }

    @Test
    fun `when file empty returns empty`() = runTest {
        tested.flow().test {
            val item = awaitItem()
            assertNull(item.value)
        }
    }

    @Test
    fun `when file NOT empty returns generated suggestion`() = runTest {
        val suggestion = MockSuggestionFactory.giveTestSuggestion()

        coEvery { cropGeneratorUC.execute(any()) } returns suggestion
        currentFileRepo.emit(MockFileFactory.giveExistingFile().toOptional())

        tested.flow().test {
            val item = awaitItem()
            assertEquals(suggestion, item.value)
        }
    }
}
