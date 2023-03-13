package com.omar.retromp3recorder.bl.settings

import android.content.SharedPreferences
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.SharedPrefsKeys
import com.omar.retromp3recorder.storage.repo.global.RecorderPrefsRepo
import com.omar.retromp3recorder.utils.domain.ServiceDealer
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ChangeAudioSourceUCTest {
    private lateinit var repo: RecorderPrefsRepo
    private val sharedPreferences: SharedPreferences = mockk()
    private val editor: SharedPreferences.Editor = mockk(relaxed = true)
    private lateinit var useCase: ChangeAudioSourceUC

    private val serviceDealer: ServiceDealer = mockk(relaxed = true)


    @Before
    fun setUp() {
        repo =
            RecorderPrefsRepo().apply { this.onNext(Mp3VoiceRecorder.RecorderPrefs()) }
        useCase = ChangeAudioSourceUC(repo, sharedPreferences, serviceDealer)

        every { sharedPreferences.edit() } returns editor
        every { editor.putInt(any(), any()) } returns editor
    }

    @Test
    fun `update update into shared prefs`() = runTest {
        val setting = Mp3VoiceRecorder.AudioSourcePref.Games

        useCase.execute(setting)

        verifyOrder {
            editor.putInt(SharedPrefsKeys.AUDIO_SOURCE, setting.ordinal)
            editor.apply()
        }
    }

    @Test
    fun `update put update into repo`() = runTest {
        val setting =  Mp3VoiceRecorder.AudioSourcePref.Games

        useCase.execute(setting)

        val first = repo.flow().first().audioSourcePref
        assertEquals(first, setting)
    }
    @Test
    fun `update stops media projection`() = runTest {
        val setting =  Mp3VoiceRecorder.AudioSourcePref.Games

        useCase.execute(setting)

        verify { serviceDealer.stopMediaProjectionService() }
    }

}
