package com.omar.retromp3recorder.bl.enablers

import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class ReverseEnablerUC @Inject constructor() {
    fun execute(): Completable = Completable.complete()
}
