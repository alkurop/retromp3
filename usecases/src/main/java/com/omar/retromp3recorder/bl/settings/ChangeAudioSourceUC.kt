package com.omar.retromp3recorder.bl.settings

import android.content.SharedPreferences
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.SharedPrefsKeys
import com.omar.retromp3recorder.storage.repo.AudioSourceRepo
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class ChangeAudioSourceUC @Inject constructor(
    private val repo: AudioSourceRepo,
    private val sharedPreferences: SharedPreferences
) {
    fun execute(audioSourcePref: Mp3VoiceRecorder.AudioSourcePref): Completable {
        return Completable.fromAction { repo.onNext(audioSourcePref) }
            .andThen(Completable.fromAction {
                sharedPreferences.edit()
                    .putInt(SharedPrefsKeys.AUDIO_SOURCE, audioSourcePref.ordinal)
                    .apply()
            })
    }
}
