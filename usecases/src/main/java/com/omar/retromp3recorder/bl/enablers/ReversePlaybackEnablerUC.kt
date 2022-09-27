package com.omar.retromp3recorder.bl.enablers

import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class ReversePlaybackEnablerUC @Inject constructor() {
    fun execute(isEnabled: Boolean): Completable = Completable.complete()
}
