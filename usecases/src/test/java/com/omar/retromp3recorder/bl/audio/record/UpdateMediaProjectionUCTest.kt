package com.omar.retromp3recorder.bl.audio.record

import android.media.projection.MediaProjection
import com.omar.retromp3recorder.bl.audio.actions.StartRecordUC
import com.omar.retromp3recorder.domain.platform.MediaProjectionState
import com.omar.retromp3recorder.storage.repo.global.MediaProjectionStateRepo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UpdateMediaProjectionUCTest {
    private lateinit var collectorProjectionRepo: MutableStateFlow<MediaProjectionState>

    private val mediaProjectionRepo = mockk<MediaProjectionStateRepo>()
    private val startRecordUC = mockk<StartRecordUC>(relaxed = true)

    private lateinit var tested: UpdateMediaProjectionUC

    @Before
    fun setUp() {
        collectorProjectionRepo = MutableStateFlow(MediaProjectionState())
        coEvery { mediaProjectionRepo.emit(any()) } coAnswers {
            collectorProjectionRepo.emit(it.invocation.args[0] as MediaProjectionState)
        }
        every { mediaProjectionRepo.flow() } coAnswers { collectorProjectionRepo }

        tested = UpdateMediaProjectionUC(mediaProjectionRepo, startRecordUC)
    }

    @Test
    fun `on execute projection repo updated`() = runTest {
        val projection = mockk<MediaProjection>()

        tested.execute(projection)

        assertEquals(projection, mediaProjectionRepo.first().mediaProjection.value)
    }

    @Test
    fun `if has projection start record executed`() = runTest {
        val projection = mockk<MediaProjection>()

        tested.execute(projection)

        assertEquals(projection, mediaProjectionRepo.first().mediaProjection.value)
        coVerify { startRecordUC.execute() }
    }

    @Test
    fun `if has no projection start record no executed`() = runTest {

        tested.execute(null)

        assertEquals(null, mediaProjectionRepo.first().mediaProjection.value)
        coVerify(exactly = 0) { startRecordUC.execute() }
    }

}
