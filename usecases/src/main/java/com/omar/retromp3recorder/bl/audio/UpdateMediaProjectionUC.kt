package com.omar.retromp3recorder.bl.audio

import android.media.projection.MediaProjection
import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.dto.MediaProjectionState
import com.omar.retromp3recorder.storage.repo.global.MediaProjectionStateRepo
import com.omar.retromp3recorder.utils.toOptional
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class UpdateMediaProjectionUC @Inject constructor(
    private val mediaProjectionRepo: MediaProjectionStateRepo,
) {
    fun execute(mediaProjection: MediaProjection?): Completable = Completable.fromAction {
        mediaProjectionRepo.onNext(
            MediaProjectionState(
                mediaProjection = mediaProjection.toOptional(),
                stop = if (mediaProjection == null) {
                    Shell(0)
                } else Shell.empty()
            )
        )
    }
}
