package com.omar.retromp3recorder.bl.audio.record

import android.media.projection.MediaProjection
import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.bl.audio.actions.StartRecordUCSuspend
import com.omar.retromp3recorder.domain.platform.MediaProjectionState
import com.omar.retromp3recorder.storage.repo.global.MediaProjectionStateRepo
import com.omar.retromp3recorder.utils.platform.toOptional
import javax.inject.Inject

class UpdateMediaProjectionUCSuspend @Inject constructor(
    private val mediaProjectionRepo: MediaProjectionStateRepo,
    private val startRecordUC: StartRecordUCSuspend
) {
    suspend fun execute(mediaProjection: MediaProjection?) {
        mediaProjectionRepo.emit(
            MediaProjectionState(
                mediaProjection = mediaProjection.toOptional(),
                stop = if (mediaProjection == null) Shell(0) else Shell.empty()
            )
        )
        if (mediaProjection != null) startRecordUC.execute()
    }
}
