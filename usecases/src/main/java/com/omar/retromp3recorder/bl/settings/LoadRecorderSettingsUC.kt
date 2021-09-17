package com.omar.retromp3recorder.bl.settings

import android.content.SharedPreferences
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.SharedPrefsKeys
import com.omar.retromp3recorder.storage.repo.AudioSourceRepo
import com.omar.retromp3recorder.storage.repo.BitRateRepo
import com.omar.retromp3recorder.storage.repo.SampleRateRepo
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

class LoadRecorderSettingsUC @Inject constructor(
    private val audioSourceRepo: AudioSourceRepo,
    private val bitRateRepo: BitRateRepo,
    private val sampleRateRepo: SampleRateRepo,
    private val scheduler: Scheduler,
    private val sharedPreferences: SharedPreferences
) {
    fun execute() = Completable
        .fromAction {
            val noSetting = -1
            sharedPreferences.getInt(SharedPrefsKeys.SAMPLE_RATE, noSetting)
                .takeIf { it != noSetting }
                ?.let {
                    val sampleRate = Mp3VoiceRecorder.SampleRate.values()[it]
                    sampleRateRepo.onNext(sampleRate)
                }
        }
        .andThen(Completable.fromAction {
            val noSetting = -1
            sharedPreferences.getInt(SharedPrefsKeys.BIT_RATE, noSetting)
                .takeIf { it != noSetting }
                ?.let {
                    val bitRate = Mp3VoiceRecorder.BitRate.values()[it]
                    bitRateRepo.onNext(bitRate)
                }
        })
        .andThen(Completable.fromAction {
            val noSetting = -1
            sharedPreferences.getInt(SharedPrefsKeys.AUDIO_SOURCE, noSetting)
                .takeIf { it != noSetting }
                ?.let {
                    val audioSource = Mp3VoiceRecorder.AudioSourcePref.values()[it]
                    audioSourceRepo.onNext(audioSource)
                }
        })
        .subscribeOn(scheduler)
}