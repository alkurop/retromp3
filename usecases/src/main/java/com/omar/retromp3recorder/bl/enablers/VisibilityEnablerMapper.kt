package com.omar.retromp3recorder.bl.enablers

import com.omar.retromp3recorder.dto.VisibilityEnabler
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class VisibilityEnablerMapper @Inject constructor(
    private val rangeEnablerUC: RangeEnablerUC,
    private val plSpeedEnablerUC: SpeedEnablerUC
) {
    fun execute(isEnabled: Boolean, enabler: VisibilityEnabler): Completable =
        when (enabler) {
            VisibilityEnabler.RangeBar -> rangeEnablerUC.execute(isEnabled)
            VisibilityEnabler.PlaybackSpeed -> plSpeedEnablerUC.execute(isEnabled)
        }
}
