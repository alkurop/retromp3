package com.omar.retromp3recorder.bl.settings

import android.content.SharedPreferences
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.SharedPrefsKeys
import com.omar.retromp3recorder.storage.repo.global.RecorderPrefsRepo
import com.omar.retromp3recorder.utils.domain.ServiceDealer
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class ChangeAudioSourceUC @Inject constructor(
    private val repo: RecorderPrefsRepo,
    private val sharedPreferences: SharedPreferences,
    private val serviceDealer: ServiceDealer
) {
    fun execute(audioSourcePref: Mp3VoiceRecorder.AudioSourcePref): Completable =
        repo
            .takeOne()
            .flatMapCompletable { prefs ->
                Completable
                    .fromAction { repo.onNext(prefs.copy(audioSourcePref = audioSourcePref)) }
                    .andThen(Completable.fromAction {
                        sharedPreferences.edit()
                            .putInt(SharedPrefsKeys.AUDIO_SOURCE, audioSourcePref.ordinal)
                            .apply()
                    })
                    .andThen(Completable.fromAction { serviceDealer.stopMediaProjectionService() })
            }
}
