package com.omar.retromp3recorder.app.ui.main

import android.media.projection.MediaProjection
import com.github.alkurop.ghostinshell.Shell

object MainView {
    data class State(
        val requestForPermissions: Shell<Set<String>>,
        val requestForScreenCapture: Shell<Any>
    )

    sealed class Input {
        data class MediaProjectionUpdated(val mediaProjection: MediaProjection?) : Input()
    }

    sealed class Output {
        data class RequestPermissionsOutput(val permissionsToRequest: Set<String>) : Output()
        data class RequestScreenCapture(val shouldRequest: Any) : Output()
    }
}