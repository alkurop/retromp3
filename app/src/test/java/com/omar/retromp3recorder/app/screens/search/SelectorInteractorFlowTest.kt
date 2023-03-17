package com.omar.retromp3recorder.app.screens.search

import app.cash.turbine.test
import com.omar.retromp3recorder.bl.files.SetCurrentFileUC
import com.omar.retromp3recorder.data.mock.MockFileFactory
import com.omar.retromp3recorder.storage.db.DatabasePagingProvider
import com.omar.retromp3recorder.storage.db.ItemsSource
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.utils.platform.toOptional
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SelectorInteractorFlowTest {
    private lateinit var currentFileRepo: CurrentFileRepo
    private val pagingProvider = mockk<DatabasePagingProvider>()
    private val inputSource = mockk<ItemsSource>()
    private val setCurrentFileUC = mockk<SetCurrentFileUC>(relaxed = true)
    private val dispatcher = UnconfinedTestDispatcher()
    private lateinit var tested: SelectorInteractorFlow
    private val file = MockFileFactory.giveExistingFile()

    @Before
    fun setUp() {
        every { pagingProvider.provideItemSource() } returns inputSource
        currentFileRepo = CurrentFileRepo()
        tested = SelectorInteractorFlow(
            currentFileRepo, pagingProvider, setCurrentFileUC, dispatcher
        )
    }

    @Test
    fun `when path null then crash`() = runTest {
        tested.processIO(flowOf(SelectorContract.Input.ItemSelected(file)))
            .test {
                val error = awaitError()
                assert(error is IllegalArgumentException)
            }
    }

    @Test
    fun `when file selected the usecase executed dismiss sent`() = runTest {
        currentFileRepo.emit(MockFileFactory.giveFutureFile().toOptional())
        tested.processIO(flowOf(SelectorContract.Input.ItemSelected(file)))
            .test {
                skipItems(2)
                val item = awaitItem()
                assert(item is SelectorContract.Output.Dismiss)
            }
        coVerify { setCurrentFileUC.execute(file) }
    }

    @Test
    fun `listen to paging provider`() = runTest {
        currentFileRepo.emit(MockFileFactory.giveExistingFile().toOptional())
        tested.processIO(flowOf(SelectorContract.Input.ItemSelected(file)))
            .test {
                skipItems(1)
                val item = awaitItem()
                assert(item is SelectorContract.Output.FileListNew)
                cancelAndConsumeRemainingEvents()
            }
    }
}

