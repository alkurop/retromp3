package com.omar.retromp3recorder.bl.files

import app.cash.turbine.test
import com.omar.retromp3recorder.data.mock.MockFileFactory
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.utils.platform.toOptional
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HasPlayableFileMapperFlowTest {
    private lateinit var currentFileRepo: CurrentFileRepo
    private lateinit var tested: HasPlayableFileMapperFlow

    @Before
    fun setUp() {
        currentFileRepo = CurrentFileRepo()
        tested = HasPlayableFileMapperFlow(currentFileRepo)
    }

    @Test
    fun `When future file THEN empty`() = runTest {
        currentFileRepo.emit(MockFileFactory.giveFutureFile().toOptional())

        tested.flow().test {
            val item = awaitItem()
            assertEquals(null, item.value)
        }
    }

    @Test
    fun `When no file THEN empty`() = runTest {
        tested.flow().test {
            val item = awaitItem()
            assertEquals(null, item.value)
        }
    }

    @Test
    fun `When Existing file empty THEN empty`() = runTest {
        val file = MockFileFactory.giveExistingFile().copy(length = 0)
        currentFileRepo.emit(file.toOptional())

        tested.flow().test {
            val item = awaitItem()
            assertEquals(null, item.value)
        }
    }

    @Test
    fun `When Existing file empty THEN file`() = runTest {
        val file = MockFileFactory.giveExistingFile().copy(length = 1)
        currentFileRepo.emit(file.toOptional())

        tested.flow().test {
            val item = awaitItem()
            assertEquals(file, item.value)
        }
    }
}
