package com.omar.retromp3recorder.app.screens.search

import androidx.paging.PagingData
import app.cash.turbine.test
import com.omar.retromp3recorder.bl.files.SetCurrentFileUC
import com.omar.retromp3recorder.data.mock.MockFileFactory
import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.storage.db.DatabasePagingProvider
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.utils.domain.toOptional
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SelectorInteractorTest {
    private lateinit var currentFileRepo: CurrentFileRepo
    private val pagingProvider = mockk<DatabasePagingProvider>()
    private val setCurrentFileUC = mockk<SetCurrentFileUC>(relaxed = true)
    private val dispatcher = UnconfinedTestDispatcher()
    private lateinit var tested: SelectorInteractor
    private val file = MockFileFactory.giveExistingFile()

    @Before
    fun setUp() {
        every { pagingProvider.createFlow(any()) } returns emptyFlow()
        currentFileRepo = CurrentFileRepo()
        tested = SelectorInteractor(
            currentFileRepo, pagingProvider, setCurrentFileUC, dispatcher
        )
    }

    @Test
    fun `listen to paging provider`() = runTest {
        currentFileRepo.emit(MockFileFactory.giveExistingFile().toOptional())
        tested.processIO(flowOf(SelectorContract.Input.ItemSelected(file)))
            .test {
                skipItems(1)
                val item = awaitItem()
                assert(item is SelectorContract.Output.CurrentFlow)
                cancelAndConsumeRemainingEvents()
            }
    }

    @Test
    fun `on item selected set current file executed`() {

    }

    @Test
    fun `on set query new flow emitted`() = runTest {
        val mockFlow = mockk<Flow<PagingData<ExistingFileWrapper>>>()
        every { pagingProvider.createFlow(any()) } returns mockFlow

        tested.processIO(flowOf(SelectorContract.Input.SetQuery("test")))
            .test {
                val fileSetter = awaitItem()
                val defaultFlow = awaitItem()
                val expected = awaitItem()
                assert(expected is SelectorContract.Output.CurrentFlow)
            }
    }
}

