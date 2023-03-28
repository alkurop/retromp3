package com.omar.retromp3recorder.app.screens.settings.components.beat_rate

import app.cash.turbine.test
import com.omar.retromp3recorder.bl.settings.ChangeBitrateUC
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
class BitRateSettingsInteractorFlowTest {
    private val dispatcher = UnconfinedTestDispatcher()
    private val usecase = mockk<ChangeBitrateUC>()
    private lateinit var repo: RecorderPrefsRepo
    private lateinit var interactor: BitRateSettingsInteractorFlow

    @Before
    fun setUp() {
        repo = RecorderPrefsRepo()
        coEvery { usecase.execute(any()) } returns Unit
        interactor = BitRateSettingsInteractorFlow(usecase, repo, dispatcher)
    }

    @Test
    fun `on input usecase executed`() = runTest {
        val event = Mp3VoiceRecorder.BitRate._160

        interactor.processIO(flowOf(event)).test {
           cancelAndIgnoreRemainingEvents()
        }

        coVerify { usecase.execute(event) }
    }

    @Test
    fun `repo listened`() = runTest {
        val event = Mp3VoiceRecorder.BitRate._160

        val settings = Mp3VoiceRecorder.RecorderPrefs(
            Mp3VoiceRecorder.SampleRate._44100,
            event,
            Mp3VoiceRecorder.AudioSourcePref.Mic
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

