package com.omar.retromp3recorder.bl.system

import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.storage.repo.MediaProjectionRequestBus
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class RequestMediaProjectionUC @Inject constructor(
    private val busMediaProjection: MediaProjectionRequestBus
) {
    fun execute(): Completable = Completable.fromAction {
        busMediaProjection.onNext(Shell(0))
    }
}
