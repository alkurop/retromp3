package com.omar.retromp3recorder.bl.settings

import android.content.SharedPreferences
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.SharedPrefsKeys
import com.omar.retromp3recorder.storage.repo.global.RecorderPrefsRepo
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ChangeSampleRateUCTest {
    private lateinit var repo: RecorderPrefsRepo
    private val sharedPreferences: SharedPreferences = mockk()
    private val editor: SharedPreferences.Editor = mockk()
    private lateinit var useCase: ChangeSampleRateUC

    @Before
    fun setUp() {
        repo = RecorderPrefsRepo()
        useCase = ChangeSampleRateUC(repo, sharedPreferences)

        every { sharedPreferences.edit() } returns editor
        every { editor.putInt(any(), any()) } returns editor
        every { editor.apply() } returns Unit
    }

    @Test
    fun `update update into shared prefs`() = runTest {
        val setting = Mp3VoiceRecorder.SampleRate._44100

        useCase.execute(setting)

        verifyOrder {
            editor.putInt(SharedPrefsKeys.SAMPLE_RATE, setting.ordinal)
            editor.apply()
        }
    }

    @Test
    fun `update put update into repo`() = runTest {
        val setting = Mp3VoiceRecorder.SampleRate._44100

        useCase.execute(setting)

        val first = repo.flow().first().sampleRate
        assertEquals(first, setting)
    }
}
