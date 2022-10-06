package com.omar.retromp3recorder.bl.enablers

import com.omar.retromp3recorder.dto.AudioEnabler
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class AudioEnablerMapper @Inject constructor(
) {
    fun execute(enabler: AudioEnabler, isEnabled: Boolean): Completable = Completable.complete()
}
