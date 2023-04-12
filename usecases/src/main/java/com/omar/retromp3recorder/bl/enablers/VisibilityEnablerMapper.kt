package com.omar.retromp3recorder.bl.enablers

import com.omar.retromp3recorder.bl.audio.effects.PlaybackSpeedVisibleUC
import com.omar.retromp3recorder.domain.VisibilityEnabler
import javax.inject.Inject

class VisibilityEnablerMapper @Inject constructor(
    private val rangeVisibilityUC: RangeVisibilitySwitcherUC,
    private val speedVisibilityUC: PlaybackSpeedVisibleUC,
) {
    suspend fun execute(enabler: VisibilityEnabler, isVisible: Boolean) {
        when (enabler) {
            VisibilityEnabler.RangeBar -> rangeVisibilityUC.execute(isVisible)
            VisibilityEnabler.PlaybackSpeed -> speedVisibilityUC.execute(isVisible)
        }
    }
}
