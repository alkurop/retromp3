package com.omar.retromp3recorder.bl.settings

import android.content.SharedPreferences
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.SharedPrefsKeys
import com.omar.retromp3recorder.storage.repo.AudioSourceRepo
import com.omar.retromp3recorder.storage.repo.BitRateRepo
import com.omar.retromp3recorder.storage.repo.SampleRateRepo
import com.omar.retromp3recorder.storage.repo.WavetableSampleRateRepo
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

private const val NO_SETTING = -1

class LoadRecorderSettingsUC @Inject constructor(
    private val audioSourceRepo: AudioSourceRepo,
    private val bitRateRepo: BitRateRepo,
    private val sampleRateRepo: SampleRateRepo,
    private val wavetableSampleRateRepo: WavetableSampleRateRepo,
    private val scheduler: Scheduler,
    private val sharedPreferences: SharedPreferences,
) {
    fun execute(): Completable = Completable
        .merge(
            listOf(
                Completable.fromAction {
                    val sampleRate =
                        sharedPreferences.getInt(SharedPrefsKeys.SAMPLE_RATE, NO_SETTING)
                            .takeIf { it != NO_SETTING }
                            ?.let { Mp3VoiceRecorder.SampleRate.values()[it] }
                            ?: Mp3VoiceRecorder.SampleRate._44100
                    sampleRateRepo.onNext(sampleRate)
                }, Completable.fromAction {
                    val bitRate = sharedPreferences.getInt(SharedPrefsKeys.BIT_RATE, NO_SETTING)
                        .takeIf { it != NO_SETTING }
                        ?.let { Mp3VoiceRecorder.BitRate.values()[it] }
                        ?: Mp3VoiceRecorder.BitRate._320
                    bitRateRepo.onNext(bitRate)
                },
                Completable.fromAction {
                    val audioSource =
                        sharedPreferences.getInt(SharedPrefsKeys.AUDIO_SOURCE, NO_SETTING)
                            .takeIf { it != NO_SETTING }
                            ?.let { Mp3VoiceRecorder.AudioSourcePref.values()[it] }
                            ?: Mp3VoiceRecorder.AudioSourcePref.Mic
                    audioSourceRepo.onNext(audioSource)
                },
                Completable.fromAction {
                    val waveTableSampleRate =
                        sharedPreferences.getInt(SharedPrefsKeys.WAVETABLE_SAMPLE_RATE, NO_SETTING)
                            .takeIf { it != NO_SETTING }
                            ?.let { Mp3VoiceRecorder.WaveTableSampleRate.values()[it] }
                            ?: Mp3VoiceRecorder.WaveTableSampleRate._100
                    wavetableSampleRateRepo.onNext(waveTableSampleRate)
                })
        )
        .subscribeOn(scheduler)
}
