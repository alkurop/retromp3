package com.omar.retromp3recorder.bl.settings

import android.content.SharedPreferences
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.SharedPrefsKeys
import com.omar.retromp3recorder.storage.repo.global.RecorderPrefsRepo
import javax.inject.Inject

private const val NO_SETTING = -1

class LoadRecorderSettingsUC @Inject constructor(
    private val repo: RecorderPrefsRepo,
    private val sharedPreferences: SharedPreferences,
) {

   suspend fun execute() {
        val sampleRate =
            sharedPreferences.getInt(SharedPrefsKeys.SAMPLE_RATE, NO_SETTING)
                .takeIf { it != NO_SETTING }
                ?.let { Mp3VoiceRecorder.SampleRate.values()[it] }
                ?: Mp3VoiceRecorder.SampleRate._44100

        val bitRate = sharedPreferences.getInt(SharedPrefsKeys.BIT_RATE, NO_SETTING)
            .takeIf { it != NO_SETTING }
            ?.let { Mp3VoiceRecorder.BitRate.values()[it] }
            ?: Mp3VoiceRecorder.BitRate._320

        val audioSource =
            sharedPreferences.getInt(SharedPrefsKeys.AUDIO_SOURCE, NO_SETTING)
                .takeIf { it != NO_SETTING }
                ?.let { Mp3VoiceRecorder.AudioSourcePref.values()[it] }
                ?: Mp3VoiceRecorder.AudioSourcePref.Mic

        val event = Mp3VoiceRecorder.RecorderPrefs(sampleRate, bitRate, audioSource)
        repo.emit(event)

    }
}
