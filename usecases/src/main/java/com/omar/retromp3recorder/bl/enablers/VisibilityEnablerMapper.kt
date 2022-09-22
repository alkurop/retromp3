package com.omar.retromp3recorder.bl.enablers

import com.omar.retromp3recorder.dto.VisibilityEnabler
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class VisibilityEnablerMapper @Inject constructor() {
    fun execute(isEnabled: Boolean, enabler: VisibilityEnabler): Completable =
        Completable.complete()
}
