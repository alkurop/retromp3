package com.omar.retromp3recorder.app.screens.home.components.menu.popups.crop

import app.cash.turbine.test
import com.github.alkurop.stringerbell.Stringer
import com.omar.retromp3recorder.bl.actions.BuyCrop10UC
import com.omar.retromp3recorder.bl.actions.CropWithProductUC
import com.omar.retromp3recorder.bl.actions.HasCropPurchaseUC
import com.omar.retromp3recorder.bl.crop.CropInPlaceUC
import com.omar.retromp3recorder.bl.crop.GenerateFileNameUC
import com.omar.retromp3recorder.bl.files.CanSaveAsNameUC
import com.omar.retromp3recorder.data.mock.MockFileFactory
import com.omar.retromp3recorder.data.mock.MockSuggestionFactory
import com.omar.retromp3recorder.utils.domain.toResult
import com.omar.retromp3recorder.storage.repo.global.ToastRepo
import com.omar.retromp3recorder.utils.domain.DifferedCoroutineScope
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import kotlin.random.Random

@OptIn(ExperimentalCoroutinesApi::class)
class CropInteractorTest {
    private val canSaveAs = mockk<CanSaveAsNameUC>()
    private val cropInPlaceUC = mockk<CropInPlaceUC>()
    private val cropOutsideUC = mockk<CropWithProductUC>()
    private val buyCrop10UC = mockk<BuyCrop10UC>(relaxed = true)
    private val hasCropPurchaseUC = mockk<HasCropPurchaseUC>()
    private val nameGenerator = mockk<GenerateFileNameUC>()
    private val toastRepo = mockk<ToastRepo>(relaxed = true)
    private val dispatcher = UnconfinedTestDispatcher()

    private lateinit var tested: CropInteractor

    private val testSuggestion = MockSuggestionFactory.giveTestSuggestion()

    @Before
    fun setUp() {
        tested = CropInteractor(
            canSaveAs,
            cropInPlaceUC,
            cropOutsideUC,
            hasCropPurchaseUC,
            buyCrop10UC,
            nameGenerator,
            toastRepo,
            dispatcher
        )
        coEvery { nameGenerator.execute() } returns testSuggestion
    }

    @Test
    fun `when has no purchase on start, emit dismiss and execute buy`() = runTest {
        coEvery { hasCropPurchaseUC.execute() } returns false
        tested.processIO(DifferedCoroutineScope(dispatcher)).test {
            val item1 = awaitItem()
            assertEquals(CropContract.Output.Dismiss, item1)
            cancelAndConsumeRemainingEvents()
        }
        coVerify(exactly = 1) { buyCrop10UC.execute() }
        coVerify(exactly = 0) { nameGenerator.execute() }
    }

    @Test
    fun `on start name suggestion is generated, cropping allowed`() = runTest {
        coEvery { hasCropPurchaseUC.execute() } returns true
        tested.processIO(DifferedCoroutineScope(dispatcher)).test {
            val item1 = awaitItem()
            assertEquals(CropContract.Output.Show, item1)

            val item2 = awaitItem()
            val result2 = item2 as? CropContract.Output.FileNameUpdate
            assertEquals(testSuggestion, result2?.nameSuggestion)

            val item3 = awaitItem()
            val result3 = item3 as? CropContract.Output.IsActionEnabled
            assertEquals(true, result3?.isEnabled)
        }

        coVerify(exactly = 0) { buyCrop10UC.execute() }
        coVerify(exactly = 1) { nameGenerator.execute() }
    }

    @Test
    fun `can crop check emits output`() = runTest {
        coEvery { hasCropPurchaseUC.execute() } returns true

        val expected = Random.nextBoolean()

        every { canSaveAs.execute(any()) } returns expected

        tested.processIO(
            DifferedCoroutineScope(dispatcher),
            flowOf(
                CropContract.Input.CheckCanCrop(
                    MockSuggestionFactory.giveTestSuggestion()
                )
            )
        ).test {
            skipItems(3)
            val item = awaitItem()
            val result = (item as? CropContract.Output.IsActionEnabled)?.isEnabled
            assertEquals(expected, result)
        }

    }

    @Test
    fun `crop in place failed emits loading, dismiss and toast`() = runTest {
        coEvery { hasCropPurchaseUC.execute() } returns true

        coEvery { cropInPlaceUC.execute(any()) } returns Result.failure(Error("Expected"))

        tested.processIO(
            DifferedCoroutineScope(dispatcher),
            flowOf(
                CropContract.Input.CropInPlace(
                    MockSuggestionFactory.giveTestSuggestion()
                )
            )
        ).test {
            skipItems(2)
            val awaitItem = awaitItem()
            assertEquals(CropContract.Output.Loading, awaitItem)
            val awaitItem1 = awaitItem()
            assertEquals(CropContract.Output.Dismiss, awaitItem1)
        }
    }

    @Test
    fun `crop in place success emits loading and dismiss`() = runTest {
        coEvery { hasCropPurchaseUC.execute() } returns true

        coEvery { cropInPlaceUC.execute(any()) } returns MockFileFactory.giveExistingFile()
            .toResult()

        tested.processIO(
            DifferedCoroutineScope(dispatcher),
            flowOf(
                CropContract.Input.CropInPlace(
                    MockSuggestionFactory.giveTestSuggestion()
                )
            )
        ).test {
            skipItems(3)
            assert(awaitItem() is CropContract.Output.Loading)
            assert(awaitItem() is CropContract.Output.Dismiss)
        }
        coVerify { toastRepo.emit(any<Stringer>()) }
    }


    @Test
    fun `crop outside place emits loading and dismiss`() = runTest {
        coEvery { hasCropPurchaseUC.execute() } returns true

        coEvery { cropOutsideUC.execute(any()) } returns MockFileFactory.giveExistingFile()
            .toResult()

        tested.processIO(
            DifferedCoroutineScope(dispatcher),
            flowOf(
                CropContract.Input.CropOutside(
                    MockSuggestionFactory.giveTestSuggestion()
                )
            )
        ).test {
            skipItems(3)
            assert(awaitItem() is CropContract.Output.Loading)
            assert(awaitItem() is CropContract.Output.Dismiss)
        }
        coVerify { toastRepo.emit(any<Stringer>()) }
    }

}
