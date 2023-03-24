package com.omar.retromp3recorder.bl.audio.record

import com.omar.retromp3recorder.bl.files.GetNewFileNameUCSuspend
import com.omar.retromp3recorder.bl.files.IncrementFileNameUCSuspend
import com.omar.retromp3recorder.bl.system.WakeLockUsecaseSuspend
import com.omar.retromp3recorder.domain.FutureFileWrapper
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.repo.global.RecorderPrefsRepo
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MicCaptureSuspendCreatorTest {

    private lateinit var recorderPrefsRepo: RecorderPrefsRepo
    private lateinit var currentFileRepo: CurrentFileRepo
    private val incrementFileNameUC = mockk<IncrementFileNameUCSuspend>(relaxed = true)
    private val getNewFileNameUC = mockk<GetNewFileNameUCSuspend>()
    private val mp3VoiceRecorder = mockk<Mp3VoiceRecorder>(relaxed = true)
    private val wakeLockUsecase = mockk<WakeLockUsecaseSuspend>(relaxed = true)

    private lateinit var tested: MicCaptureSuspendCreator

    @Before
    fun setUp() {
        recorderPrefsRepo = RecorderPrefsRepo()
        currentFileRepo = CurrentFileRepo()

        tested = MicCaptureSuspendCreator(
            recorderPrefsRepo,
            currentFileRepo,
            incrementFileNameUC,
            getNewFileNameUC,
            mp3VoiceRecorder,
            wakeLockUsecase
        )
    }

    @Test
    fun `on execute current file updated with future file`() = runTest {
        val fileName = "test"
        val audioSource = mockk<Mp3VoiceRecorder.AudioSource>()
        recorderPrefsRepo.emit(Mp3VoiceRecorder.RecorderPrefs())

        coEvery { getNewFileNameUC.execute() } returns fileName

        tested.execute(audioSource)

        val currentFile = (currentFileRepo.first().value as? FutureFileWrapper)?.path
        assertEquals(fileName, currentFile)
    }

    @Test
    fun `on execute sequence executed`() = runTest {
        val fileName = "test"
        val audioSource = mockk<Mp3VoiceRecorder.AudioSource>()
        recorderPrefsRepo.emit(Mp3VoiceRecorder.RecorderPrefs())

        coEvery { getNewFileNameUC.execute() } returns fileName

        tested.execute(audioSource)

        coVerifySequence {
            mp3VoiceRecorder.recordWithProps(any())
            incrementFileNameUC.execute()
            wakeLockUsecase.execute()
        }
    }
}
