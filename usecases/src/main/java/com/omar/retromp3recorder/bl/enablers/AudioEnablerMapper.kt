package com.omar.retromp3recorder.bl.enablers

import com.omar.retromp3recorder.dto.AudioEnabler
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class AudioEnablerMapper @Inject constructor(
    private val reverseEnablerUC: ReversePlaybackEnablerUC,
    private val loopEnablerUC: LoopEnablerUC
) {
    fun execute(isEnabled: Boolean, enabler: AudioEnabler): Completable =
        when (enabler) {
            AudioEnabler.Reverse -> reverseEnablerUC.execute(isEnabled)
            AudioEnabler.Loop -> loopEnablerUC.execute(isEnabled)
        }
}
