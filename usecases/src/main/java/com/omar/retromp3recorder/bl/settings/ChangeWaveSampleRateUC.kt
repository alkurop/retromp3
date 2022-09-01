package com.omar.retromp3recorder.bl.settings

import android.content.SharedPreferences
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.SharedPrefsKeys
import com.omar.retromp3recorder.storage.repo.SampleRateRepo
import com.omar.retromp3recorder.storage.repo.WavetableSampleRateRepo
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class ChangeWaveSampleRateUC @Inject constructor(
    private val repo: WavetableSampleRateRepo,
    private val sharedPreferences: SharedPreferences
) {
    fun execute(sampleRate: Mp3VoiceRecorder.WaveTableSampleRate): Completable {
        return Completable.fromAction { repo.onNext(sampleRate) }
            .andThen(Completable.fromAction {
                sharedPreferences.edit()
                    .putInt(SharedPrefsKeys.WAVETABLE_SAMPLE_RATE, sampleRate.ordinal)
                    .apply()
            })
    }
}
