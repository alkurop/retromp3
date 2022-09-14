package com.omar.retromp3recorder.bl.system

import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.storage.repo.MediaProjectionState
import com.omar.retromp3recorder.storage.repo.MediaProjectionStateRepo
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class RequestMediaProjectionUC @Inject constructor(
    private val busMediaProjection: MediaProjectionStateRepo
) {
    fun execute(): Completable = Completable.fromAction {
        busMediaProjection.onNext(MediaProjectionState(request = Shell(0)))
    }
}
