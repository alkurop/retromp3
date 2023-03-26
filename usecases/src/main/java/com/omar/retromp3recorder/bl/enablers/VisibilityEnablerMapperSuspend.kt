package com.omar.retromp3recorder.bl.enablers

import com.omar.retromp3recorder.domain.VisibilityEnabler
import javax.inject.Inject

class VisibilityEnablerMapperSuspend @Inject constructor(
    private val rangeEnablerUC: RangeEnablerUCSuspend,
) {
    suspend fun execute(enabler: VisibilityEnabler, isEnabled: Boolean) {
        when (enabler) {
            VisibilityEnabler.RangeBar -> rangeEnablerUC.execute(isEnabled)
            VisibilityEnabler.PlaybackSpeed -> {
            /* noop */
            }
        }
    }
}
