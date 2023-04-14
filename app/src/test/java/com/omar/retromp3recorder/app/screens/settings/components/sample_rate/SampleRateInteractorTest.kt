package com.omar.retromp3recorder.app.screens.settings.components.sample_rate

import app.cash.turbine.test
import com.omar.retromp3recorder.bl.settings.ChangeSampleRateUC
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
class SampleRateInteractorTest {
    private val dispatcher = UnconfinedTestDispatcher()
    private val usecase = mockk<ChangeSampleRateUC>()
    private lateinit var repo: RecorderPrefsRepo
    private lateinit var interactor: SampleRateInteractor

    @Before
    fun setUp() {
        repo = RecorderPrefsRepo()
        coEvery { usecase.execute(any()) } returns Unit
        interactor = SampleRateInteractor(usecase, repo, dispatcher)
    }

    @Test
    fun `on input usecase executed`() = runTest {
        val event = Mp3VoiceRecorder.SampleRate._44100

        interactor.processIO(DifferedCoroutineScope(dispatcher), flowOf(event)).test {
            cancelAndConsumeRemainingEvents()
        }

        coVerify { usecase.execute(event) }
    }

    @Test
    fun `repo listened`() = runTest {
        val event = Mp3VoiceRecorder.SampleRate._11025

        val settings = Mp3VoiceRecorder.RecorderPrefs(
            event,
            Mp3VoiceRecorder.BitRate._128,
            Mp3VoiceRecorder.AudioSourcePref.Mic
        )

        repo.emit(
            settings
        )

        interactor.processIO(DifferedCoroutineScope(dispatcher), flowOf()).test {
            val item = awaitItem()
            assertEquals(event, item)
        }
    }
}
