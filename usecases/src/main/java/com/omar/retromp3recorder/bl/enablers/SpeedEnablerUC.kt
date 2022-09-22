package com.omar.retromp3recorder.bl.enablers

import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class SpeedEnablerUC @Inject constructor() {
    fun execute(isEnabled: Boolean): Completable = Completable.complete()
}
