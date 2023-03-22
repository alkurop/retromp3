package com.omar.retromp3recorder.bl.audio

import android.media.projection.MediaProjection
import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.bl.audio.actions.StartRecordUC
import com.omar.retromp3recorder.storage.repo.global.MediaProjectionStateRepo
import com.omar.retromp3recorder.utils.platform.toOptional
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class UpdateMediaProjectionUC @Inject constructor(
    private val mediaProjectionRepo: MediaProjectionStateRepo,
    private val startRecordUC: StartRecordUC
) {
    fun execute(mediaProjection: MediaProjection?): Completable = Completable.fromAction {
        mediaProjectionRepo.tryNext(
            com.omar.retromp3recorder.domain.platform.MediaProjectionState(
                mediaProjection = mediaProjection.toOptional(),
                stop = if (mediaProjection == null) {
                    Shell(0)
                } else Shell.empty()
            )
        )
    }.andThen(if (mediaProjection != null) startRecordUC.execute() else Completable.complete())
}
