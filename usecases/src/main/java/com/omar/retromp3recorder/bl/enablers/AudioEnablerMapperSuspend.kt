package com.omar.retromp3recorder.bl.enablers

import com.omar.retromp3recorder.domain.AudioEnabler
import javax.inject.Inject

class AudioEnablerMapperSuspend @Inject constructor(
) {
    fun execute(enabler: AudioEnabler, isEnabled: Boolean) {}
}
