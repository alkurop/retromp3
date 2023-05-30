package com.omar.retromp3recorder.app.screens.settings.components.audio_source

import app.cash.turbine.test
import com.omar.retromp3recorder.bl.settings.ChangeAudioSourceUC
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.repo.global.RecorderPrefsRepo
import com.omar.retromp3recorder.utils.domain.DifferedCoroutineScope
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AudioSourceInteractorTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher = UnconfinedTestDispatcher()
    private val usecase = mockk<ChangeAudioSourceUC>()
    private lateinit var repo: RecorderPrefsRepo
    private lateinit var interactor: AudioSourceInteractor

    @Before
    fun setUp() {
        repo = RecorderPrefsRepo()
        coEvery { usecase.execute(any()) } returns Unit
        interactor = AudioSourceInteractor(usecase, repo, dispatcher)
    }

    @Test
    fun `on input usecase executed`() = runTest {
        val event = Mp3VoiceRecorder.AudioSourcePref.Media

        interactor.processIO(flowOf(event)).test {
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { usecase.execute(event) }
    }

    @Test
    fun `repo listened`() = runTest {
        val event = Mp3VoiceRecorder.AudioSourcePref.Media

        val settings = Mp3VoiceRecorder.RecorderPrefs(
            Mp3VoiceRecorder.SampleRate._44100,
            Mp3VoiceRecorder.BitRate._128,
            event
        )
        repo.emit(
            settings
        )

        interactor.processIO(flowOf()).test {
            val item = awaitItem()
            assertEquals(event, item)

        }
    }
}
