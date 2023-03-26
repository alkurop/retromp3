package com.omar.retromp3recorder.bl.settings

import android.content.SharedPreferences
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.SharedPrefsKeys
import com.omar.retromp3recorder.storage.repo.global.RecorderPrefsRepo
import com.omar.retromp3recorder.utils.domain.ServiceDealer
import com.omar.retromp3recorder.utils.platform.repo.first
import javax.inject.Inject

class ChangeAudioSourceUC @Inject constructor(
    private val repo: RecorderPrefsRepo,
    private val sharedPreferences: SharedPreferences,
    private val serviceDealer: ServiceDealer
) {
    suspend fun execute(audioSourcePref: Mp3VoiceRecorder.AudioSourcePref) {
        val prefs = repo.first()
        repo.emit(prefs.copy(audioSourcePref = audioSourcePref))
        sharedPreferences.edit()
            .putInt(SharedPrefsKeys.AUDIO_SOURCE, audioSourcePref.ordinal)
            .apply()

        serviceDealer.stopMediaProjectionService()
    }
}
