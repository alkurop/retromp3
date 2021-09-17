package com.omar.retromp3recorder.bl.audio

import android.media.projection.MediaProjection
import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.storage.repo.MediaProjectionRepo
import com.omar.retromp3recorder.storage.repo.MediaProjectionStopBus
import com.omar.retromp3recorder.utils.Optional
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class UpdateMediaProjectionUC @Inject constructor(
    private val mediaProjectionRepo: MediaProjectionRepo,
    private val mediaProjectionStopBus: MediaProjectionStopBus,
) {
    fun execute(mediaProjection: MediaProjection?): Completable = Completable.fromAction {
        mediaProjectionRepo.onNext(Optional(mediaProjection))
        if (mediaProjection == null) {
            mediaProjectionStopBus.onNext(Shell(0))
        }
    }
}