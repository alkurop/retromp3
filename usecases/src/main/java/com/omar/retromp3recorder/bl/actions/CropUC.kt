package com.omar.retromp3recorder.bl.actions

import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class CropUC @Inject constructor() {
    fun execute(): Completable = Completable.complete()
}
