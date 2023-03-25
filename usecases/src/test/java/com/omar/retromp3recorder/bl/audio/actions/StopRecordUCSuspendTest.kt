package com.omar.retromp3recorder.bl.audio.actions

import com.omar.retromp3recorder.data.mock.MockFileFactory
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.utils.domain.ServiceDealer
import com.omar.retromp3recorder.utils.platform.toOptional
import io.mockk.coVerifySequence
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class StopRecordUCSuspendTest {
    private lateinit var currentFileRepo: CurrentFileRepo
    private lateinit var currentFileRepoSpy: CurrentFileRepo
    private val voiceRecorder = mockk<Mp3VoiceRecorder>(relaxed = true)
    private val serviceDealer = mockk<ServiceDealer>(relaxed = true)
    lateinit var tested: StopRecordUCSuspend

    @Before
    fun setUp() {
        currentFileRepo = CurrentFileRepo()
        currentFileRepoSpy = spyk(currentFileRepo)
        tested = StopRecordUCSuspend(currentFileRepoSpy, voiceRecorder, serviceDealer)
    }

    @Test
    fun `when executed sequence executed`() = runTest {

        val file = MockFileFactory.giveExistingFile().toOptional()
        currentFileRepo.emit(file)

        tested.execute()

        coVerifySequence {
            voiceRecorder.stopRecord()
            serviceDealer.stopWakelockService()
            currentFileRepoSpy.first()
            currentFileRepoSpy.emit(withArg { assertEquals(it, file) })
        }
    }
}
