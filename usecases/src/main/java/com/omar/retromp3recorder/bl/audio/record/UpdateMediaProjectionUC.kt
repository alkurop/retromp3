package com.omar.retromp3recorder.bl.audio.record

import android.media.projection.MediaProjection
import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.bl.audio.actions.StartRecordUC
import com.omar.retromp3recorder.domain.platform.MediaProjectionState
import com.omar.retromp3recorder.storage.repo.global.MediaProjectionStateRepo
import com.omar.retromp3recorder.utils.domain.toOptional
import javax.inject.Inject

class UpdateMediaProjectionUC @Inject constructor(
    private val mediaProjectionRepo: MediaProjectionStateRepo,
    private val startRecordUC: StartRecordUC
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
