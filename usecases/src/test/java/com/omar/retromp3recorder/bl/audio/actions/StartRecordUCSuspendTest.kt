package com.omar.retromp3recorder.bl.audio.actions

import android.media.AudioAttributes
import com.omar.retromp3recorder.bl.audio.record.RecordMediaUC
import com.omar.retromp3recorder.bl.audio.record.RecordMicUC
import com.omar.retromp3recorder.bl.enablers.DeactivatePlayerControlsUCSuspend
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.repo.global.RecorderPrefsRepo
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class StartRecordUCSuspendTest {
    private lateinit var recorderPrefsRepo: RecorderPrefsRepo
    private val deactivatePlayerControlsUC = mockk<DeactivatePlayerControlsUCSuspend>(relaxed = true)
    private val recordMediaUC = mockk<RecordMediaUC>(relaxed = true)
    private val recordMicUC = mockk<RecordMicUC>(relaxed = true)

    private lateinit var tested: StartRecordUCSuspend

    @Before
    fun setUp() {
        recorderPrefsRepo = RecorderPrefsRepo()
        tested = StartRecordUCSuspend(
            recorderPrefsRepo, deactivatePlayerControlsUC, recordMediaUC, recordMicUC
        )
    }

    @Test
    fun `on execute player controls deactivated`() = runTest {
        recorderPrefsRepo.emit(Mp3VoiceRecorder.RecorderPrefs())

        tested.execute()

        coVerify { deactivatePlayerControlsUC.execute() }
    }

    @Test
    fun `on mic execute mic`() = runTest {
        recorderPrefsRepo.emit(Mp3VoiceRecorder.RecorderPrefs(audioSourcePref = Mp3VoiceRecorder.AudioSourcePref.Mic))

        tested.execute()

        coVerify(exactly = 1) { recordMicUC.execute() }
        coVerify(exactly = 0) { recordMediaUC.execute(AudioAttributes.USAGE_MEDIA) }
        coVerify(exactly = 0) { recordMediaUC.execute(AudioAttributes.USAGE_GAME) }
    }

    @Test
    fun `on media execute media`() = runTest {
        recorderPrefsRepo.emit(Mp3VoiceRecorder.RecorderPrefs(audioSourcePref = Mp3VoiceRecorder.AudioSourcePref.Games))

        tested.execute()

        coVerify(exactly = 0) { recordMicUC.execute() }
        coVerify(exactly = 1) { recordMediaUC.execute(AudioAttributes.USAGE_GAME) }
    }

    @Test
    fun `on games execute games`() = runTest {
        recorderPrefsRepo.emit(Mp3VoiceRecorder.RecorderPrefs(audioSourcePref = Mp3VoiceRecorder.AudioSourcePref.Media))

        tested.execute()

        coVerify(exactly = 0) { recordMicUC.execute() }
        coVerify(exactly = 1) { recordMediaUC.execute(AudioAttributes.USAGE_MEDIA) }
    }

}
