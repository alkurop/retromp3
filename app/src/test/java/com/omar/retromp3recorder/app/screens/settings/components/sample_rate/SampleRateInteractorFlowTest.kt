package com.omar.retromp3recorder.app.screens.settings.components.sample_rate

import app.cash.turbine.test
import com.omar.retromp3recorder.bl.settings.ChangeSampleRateUC
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.repo.global.RecorderPrefsRepo
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
class SampleRateInteractorFlowTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher = UnconfinedTestDispatcher()
    private val usecase = mockk<ChangeSampleRateUC>()
    private lateinit var repo: RecorderPrefsRepo
    private lateinit var interactor: SampleRateInteractorFlow

    @Before
    fun setUp() {
        repo = RecorderPrefsRepo()
        coEvery { usecase.execute(any()) } returns Unit
        interactor = SampleRateInteractorFlow(usecase, repo, dispatcher)
    }

    @Test
    fun `on input usecase executed`() = runTest {
        val event = Mp3VoiceRecorder.SampleRate._44100

        interactor.processIO(flowOf(event)).test {
            expectNoEvents()
        }

        coVerify { usecase.execute(event) }
    }

    @Test
    fun `on input repo updated`() = runTest {
        val settings = Mp3VoiceRecorder.RecorderPrefs(
            Mp3VoiceRecorder.SampleRate._44100,
            Mp3VoiceRecorder.BitRate._128,
            Mp3VoiceRecorder.AudioSourcePref.Mic
        )
        val event = Mp3VoiceRecorder.SampleRate._11025

        repo.onNext(
            settings
        )

        interactor.processIO(flowOf(event)).test {
            val item = awaitItem()
            assertEquals(event, item)
        }
    }
}
