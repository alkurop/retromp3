package com.omar.retromp3recorder.bl.audio.record

import android.media.projection.MediaProjection
import com.omar.retromp3recorder.bl.system.RequestMediaProjectionUC
import com.omar.retromp3recorder.domain.platform.MediaProjectionState
import com.omar.retromp3recorder.storage.repo.global.MediaProjectionStateRepo
import com.omar.retromp3recorder.utils.domain.ServiceDealer
import com.omar.retromp3recorder.utils.domain.toOptional
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RecordMediaUCTest {
    private val serviceDealer = mockk<ServiceDealer>(relaxed = true)
    private val projectionRepo = mockk<MediaProjectionStateRepo>()
    private lateinit var collectorProjectionRepo: MutableStateFlow<MediaProjectionState>
    private val micCaptureCompletableCreator = mockk<AudioCaptureUC>(relaxed = true)
    private val requestMediaProjectionUC = mockk<RequestMediaProjectionUC>(relaxed = true)
    private val wavetableUC = mockk<RecordWavetableUC>(relaxed = true)

    lateinit var tested: RecordMediaUC

    @Before
    fun setUp() {
        collectorProjectionRepo = MutableStateFlow(MediaProjectionState())
        coEvery { projectionRepo.emit(any()) } coAnswers {
            collectorProjectionRepo.emit(it.invocation.args[0] as MediaProjectionState)
        }

        every { projectionRepo.flow() } coAnswers { collectorProjectionRepo }
        tested = RecordMediaUC(
            serviceDealer,
            projectionRepo,
            micCaptureCompletableCreator,
            requestMediaProjectionUC,
            wavetableUC
        )
    }

    @Test
    fun `projection service started`() = runTest {
        tested.execute(0)
        verify { serviceDealer.startMediaProjectionService() }
    }

    @Test
    fun `WHEN projection null THEN request projection executed`() = runTest {
        tested.execute(0)

        coVerify(exactly = 1) { requestMediaProjectionUC.execute() }
        coVerify(exactly = 0) { micCaptureCompletableCreator.execute(any()) }
        coVerify(exactly = 0) { wavetableUC.execute() }

    }

    @Test
    fun `WHEN hes projection null THEN sequence executed`() = runTest {
        projectionRepo.emit(MediaProjectionState(mediaProjection = mockk<MediaProjection>().toOptional()))
        tested.execute(0)

        coVerify(exactly = 0) { requestMediaProjectionUC.execute() }
        coVerify(exactly = 1) { micCaptureCompletableCreator.execute(any()) }
        coVerify(exactly = 1) { wavetableUC.execute() }
    }
}
