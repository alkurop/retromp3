package com.omar.retromp3recorder.bl.settings

import android.content.SharedPreferences
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.SharedPrefsKeys
import com.omar.retromp3recorder.storage.repo.global.RecorderPrefsRepo
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class ChangeBitrateUC @Inject constructor(
    private val repo: RecorderPrefsRepo,
    private val sharedPreferences: SharedPreferences
) {
    fun execute(bitRate: Mp3VoiceRecorder.BitRate): Completable =
        repo.takeSingle().flatMapCompletable { prefs ->
            Completable.fromAction { repo.tryNext(prefs.copy(bitRate = bitRate)) }
                .andThen(Completable
                    .fromAction {
                        sharedPreferences.edit()
                            .putInt(SharedPrefsKeys.BIT_RATE, bitRate.ordinal)
                            .apply()
                    }
                )
        }
}
