package com.omar.retromp3recorder.bl.audio.record

import android.media.projection.MediaProjection
import com.omar.retromp3recorder.bl.audio.actions.StartRecordUCSuspend
import com.omar.retromp3recorder.storage.repo.global.MediaProjectionStateRepo
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UpdateMediaProjectionUCSuspendTest {
    private lateinit var mediaProjectionRepo: MediaProjectionStateRepo
    private val startRecordUC = mockk<StartRecordUCSuspend>(relaxed = true)

    private lateinit var tested: UpdateMediaProjectionUCSuspend

    @Before
    fun setUp() {
        mediaProjectionRepo = MediaProjectionStateRepo()
        tested = UpdateMediaProjectionUCSuspend(mediaProjectionRepo, startRecordUC)
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
