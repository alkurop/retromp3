package com.omar.retromp3recorder.app.screens.home.components.menu.popups.crop

import app.cash.turbine.test
import com.github.alkurop.stringerbell.Stringer
import com.omar.retromp3recorder.bl.actions.CropUC
import com.omar.retromp3recorder.bl.crop.CropInPlaceUC
import com.omar.retromp3recorder.bl.crop.GenerateFileNameUC
import com.omar.retromp3recorder.bl.files.CanSaveAsNameUC
import com.omar.retromp3recorder.data.mock.MockFileFactory
import com.omar.retromp3recorder.data.mock.MockSuggestionFactory
import com.omar.retromp3recorder.storage.repo.global.ToastRepo
import com.omar.retromp3recorder.utils.domain.Optional
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
class CropInteractorFlowTest {
    private val canSaveAs = mockk<CanSaveAsNameUC>()
    private val cropInPlaceUC = mockk<CropInPlaceUC>()
    private val cropOutsideUC = mockk<CropUC>()
    private val nameGenerator = mockk<GenerateFileNameUC>()
    private val toastRepo = mockk<ToastRepo>(relaxed = true)
    private val dispatcher = UnconfinedTestDispatcher()

    private lateinit var tested: CropInteractorFlow

    private val testSuggestion = MockSuggestionFactory.giveTestSuggestion()

    @Before
    fun setUp() {
        tested = CropInteractorFlow(
            canSaveAs,
            cropInPlaceUC,
            cropOutsideUC,
            nameGenerator,
            toastRepo,
            dispatcher
        )
        coEvery { nameGenerator.execute() } returns testSuggestion
    }

    @Test
    fun `on start name suggestion is generated, cropping allowed`() = runTest {
        tested.processIO(flowOf()).test {
            val item = awaitItem()
            val result = item as? CropContract.Output.FileNameUpdate
            assertEquals(testSuggestion, result?.nameSuggestion)

            val item2 = awaitItem()
            val result2 = item2 as? CropContract.Output.IsActionEnabled
            assertEquals(true, result2?.isEnabled)
        }
    }

    @Test
    fun `can crop check emits output`() = runTest {
        val expected = Random.nextBoolean()
        every { canSaveAs.execute(any()) } returns expected

        tested.processIO(
            flowOf(
                CropContract.Input.CheckCanCrop(
                    MockSuggestionFactory
                        .giveTestSuggestion()
                )
            )
        )
            .test {
                skipItems(2)
                val item = awaitItem()
                val result = (item as? CropContract.Output.IsActionEnabled)?.isEnabled
                assertEquals(expected, result)
            }

    }

    @Test
    fun `crop in place failed emits loading and dismiss`() = runTest {
        coEvery { cropInPlaceUC.execute(any()) } returns Optional.empty()

        tested.processIO(
            flowOf(
                CropContract.Input.CropInPlace(
                    MockSuggestionFactory
                        .giveTestSuggestion()
                )
            )
        )
            .test {
                skipItems(2)
                assert(awaitItem() is CropContract.Output.Loading)
                assert(awaitItem() is CropContract.Output.Dismiss)
            }
    }

    @Test
    fun `crop in place success emits loading and dismiss`() = runTest {
        coEvery { cropInPlaceUC.execute(any()) } returns Optional(MockFileFactory.giveExistingFile())

        tested.processIO(
            flowOf(
                CropContract.Input.CropInPlace(
                    MockSuggestionFactory
                        .giveTestSuggestion()
                )
            )
        )
            .test {
                skipItems(2)
                assert(awaitItem() is CropContract.Output.Loading)
                assert(awaitItem() is CropContract.Output.Dismiss)
            }
        coVerify { toastRepo.emit(any<Stringer>()) }
    }


    @Test
    fun `crop outside place emits loading and dismiss`() = runTest {
        coEvery { cropOutsideUC.execute(any()) } returns Optional(MockFileFactory.giveExistingFile())

        tested.processIO(
            flowOf(
                CropContract.Input.CropOutside(
                    MockSuggestionFactory
                        .giveTestSuggestion()
                )
            )
        )
            .test {
                skipItems(2)
                assert(awaitItem() is CropContract.Output.Loading)
                assert(awaitItem() is CropContract.Output.Dismiss)
            }
        coVerify { toastRepo.emit(any<Stringer>()) }
    }

}
