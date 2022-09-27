package com.omar.retromp3recorder.bl.settings

import android.content.SharedPreferences
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.SharedPrefsKeys
import com.omar.retromp3recorder.storage.repo.global.RecorderPrefsRepo
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class ChangeSampleRateUC @Inject constructor(
    private val repo: RecorderPrefsRepo,
    private val sharedPreferences: SharedPreferences
) {
    fun execute(sampleRate: Mp3VoiceRecorder.SampleRate): Completable =
        repo.takeOne().flatMapCompletable { prefs ->
            Completable.fromAction { repo.onNext(prefs.copy(sampleRate = sampleRate)) }
                .andThen(Completable.fromAction {
                    sharedPreferences.edit()
                        .putInt(SharedPrefsKeys.SAMPLE_RATE, sampleRate.ordinal)
                        .apply()
                })
        }
}
