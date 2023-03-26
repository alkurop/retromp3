package com.omar.retromp3recorder.bl.settings

import android.content.SharedPreferences
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.SharedPrefsKeys
import com.omar.retromp3recorder.storage.repo.global.RecorderPrefsRepo
import com.omar.retromp3recorder.utils.domain.repo.first
import javax.inject.Inject

class ChangeBitrateUC @Inject constructor(
    private val repo: RecorderPrefsRepo,
    private val sharedPreferences: SharedPreferences
) {
    suspend fun execute(bitRate: Mp3VoiceRecorder.BitRate) {
        val prefs = repo.first()
        repo.emit(prefs.copy(bitRate = bitRate))
        sharedPreferences.edit()
            .putInt(SharedPrefsKeys.BIT_RATE, bitRate.ordinal)
            .apply()
    }
}
