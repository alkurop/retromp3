package com.omar.retromp3recorder.bl.audio.progress

import com.omar.retromp3recorder.bl.waveform.RecordWavetableMapper
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.storage.repo.local.PlayerProgressRepo
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class JoinedProgressMapperTest {
    private val audioStateMapper = mockk<AudioStateMapper>()
    private lateinit var currentFileRepo: CurrentFileRepo
    private val playerProgressRepo = mockk<PlayerProgressRepo>()
    private val recorderWavetableMapper = mockk<RecordWavetableMapper>()
    private val dispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        currentFileRepo = CurrentFileRepo()
    }

    @Test
    fun `When Playing THEN progress shown`() = runTest { }

    @Test
    fun `When Idle has file THEN progress shown`() = runTest { }

    @Test
    fun `When Idle not file THEN hidden`() = runTest { }

    @Test
    fun `When Idle future file THEN intermediate`() = runTest { }

    @Test
    fun `When Recording then recorder progress shown`() = runTest { }

    @Test
    fun `When Recording then idle THEN collection waveform stops (bug)`() = runTest { }

}
