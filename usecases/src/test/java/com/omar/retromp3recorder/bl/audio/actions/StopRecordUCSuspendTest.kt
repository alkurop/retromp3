package com.omar.retromp3recorder.bl.audio.actions

import com.omar.retromp3recorder.data.mock.MockFileFactory
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.utils.domain.ServiceDealer
import com.omar.retromp3recorder.utils.domain.toOptional
import io.mockk.coVerifySequence
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class StopRecordUCSuspendTest {
    private lateinit var currentFileRepo: CurrentFileRepo
    private val voiceRecorder = mockk<Mp3VoiceRecorder>(relaxed = true)
    private val serviceDealer = mockk<ServiceDealer>(relaxed = true)
    lateinit var tested: StopRecordUCSuspend

    @Before
    fun setUp() {
        currentFileRepo = CurrentFileRepo()
        tested = StopRecordUCSuspend(currentFileRepo, voiceRecorder, serviceDealer)
    }

    @Test
    fun `when executed sequence executed`() = runTest {

        val file = MockFileFactory.giveExistingFile()
        currentFileRepo.emit(file.toOptional())

        tested.execute()

        coVerifySequence {
            voiceRecorder.stopRecord()
            serviceDealer.stopWakelockService()
        }
        val result = currentFileRepo.flow() .first()
        assertEquals(result.value, file)
    }
}
