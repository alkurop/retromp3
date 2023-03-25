package com.omar.retromp3recorder.bl.system

import com.omar.retromp3recorder.data.mock.MockFileFactory
import com.omar.retromp3recorder.share.Sharer
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.utils.platform.toOptional
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ShareUCSuspendTest {
    private val sharingModule = mockk<Sharer>(relaxed = true)
    private lateinit var currentFileRepo: CurrentFileRepo

    private lateinit var tested: ShareUCSuspend

    @Before
    fun setUp() {
        currentFileRepo = CurrentFileRepo()
        tested = ShareUCSuspend(sharingModule, currentFileRepo)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `when no file crash`() = runTest {
        tested.execute()
    }

    @Test(expected = IllegalArgumentException::class)
    fun `when future file crash`() = runTest {
        currentFileRepo.emit(MockFileFactory.giveFutureFile().toOptional())
        tested.execute()
    }

    @Test
    fun `when existing file then sequence executed`() = runTest {
        val file = MockFileFactory.giveExistingFile()
        currentFileRepo.emit(file.toOptional())
        tested.execute()
        coVerify {
            sharingModule.share(withArg {
                val expected = file.path
                val result = it.path

                assertEquals(expected, result)
            })
        }
    }
}
