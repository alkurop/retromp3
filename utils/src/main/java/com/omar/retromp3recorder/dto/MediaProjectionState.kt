package com.omar.retromp3recorder.dto

import android.media.projection.MediaProjection
import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.utils.Optional

data class MediaProjectionState(
    val mediaProjection: Optional<MediaProjection> = Optional.empty(),
    val request: Shell<Any> = Shell.empty(),
    val stop: Shell<Any> = Shell.empty()
)
