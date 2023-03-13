package com.omar.retromp3recorder.bl.settings

import android.content.SharedPreferences
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.SharedPrefsKeys
import com.omar.retromp3recorder.storage.repo.global.RecorderPrefsRepo
import io.reactivex.rxjava3.core.Completable
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ChangeSampleRateUC @Inject constructor(
    private val repo: RecorderPrefsRepo,
    private val sharedPreferences: SharedPreferences
) {
    suspend fun execute(sampleRate: Mp3VoiceRecorder.SampleRate) {
        val prefs = repo.takeOneFlow().first()
        repo.onNext(prefs.copy(sampleRate = sampleRate))
        sharedPreferences.edit()
            .putInt(SharedPrefsKeys.SAMPLE_RATE, sampleRate.ordinal)
            .apply()
    }
}
