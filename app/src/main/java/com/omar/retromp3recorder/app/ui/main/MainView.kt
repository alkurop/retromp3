package com.omar.retromp3recorder.app.ui.main

import android.media.projection.MediaProjection
import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.dto.FeatureFlagsCollection

object MainView {
    data class State(
        val requestForPermissions: Shell<Set<String>>,
        val requestForScreenCapture: Shell<Any>,
        val isNewLayout: Boolean = false,
        val isLogViewEnabled: Boolean = false,
        val shouldKeepScreenOn: Boolean = false,
        val shouldRestart: Boolean = false,
    )

    sealed class Input {
        object CheckAllPermisionsOnStartup:Input()
        data class MediaProjectionUpdated(val mediaProjection: MediaProjection?) : Input()
    }

    sealed class Output {
        data class RequestPermissionsOutput(val permissionsToRequest: Set<String>) : Output()
        data class RequestScreenCapture(val shouldRequest: Any) : Output()
        data class SettingsUpdated(val featureFlagsCollection: FeatureFlagsCollection) : Output()
    }
}
