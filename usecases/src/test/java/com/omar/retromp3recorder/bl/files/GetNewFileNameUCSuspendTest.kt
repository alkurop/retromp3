package com.omar.retromp3recorder.bl.files

import android.content.SharedPreferences
import com.omar.retromp3recorder.storage.SharedPrefsKeys
import com.omar.retromp3recorder.utils.platform.DirPathProvider
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetNewFileNameUCSuspendTest {
    private val dirPathProvider = mockk<DirPathProvider>()
    private val sharedPreferences = mockk<SharedPreferences>()

    private lateinit var tested: GetNewFileNameUCSuspend

    @Before
    fun setUp() {
        tested = GetNewFileNameUCSuspend(dirPathProvider, sharedPreferences)
    }

    @Test
    fun `filename contains path from dir provider`() = runTest {
        val expectedPath = "path"
        every { dirPathProvider.providerDirPath() } returns expectedPath
        every { sharedPreferences.getInt(SharedPrefsKeys.FILE_NAME, 1) } returns 1

        val result = tested.execute()
        val split = result.split("/")
        val path = split[0]

        assertEquals(path, expectedPath)
    }

    @Test
    fun `filename ends with mp3 extension`() = runTest {
        val expectedPath = "path"
        every { dirPathProvider.providerDirPath() } returns expectedPath
        every { sharedPreferences.getInt(SharedPrefsKeys.FILE_NAME, 1) } returns 1

        val result = tested.execute()
        val split = result.split("/")
        val name = split[1]

        assert(name.endsWith(".mp3"))
    }
}
