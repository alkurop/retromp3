package com.omar.retromp3recorder.app.ui.main

import android.media.projection.MediaProjection
import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.storage.repo.FeatureFlagsCollection

object MainView {
    data class State(
        val requestForPermissions: Shell<Set<String>>,
        val requestForScreenCapture: Shell<Any>,
        val isLogViewEnabled: Boolean = false,
        val isNewEnabledLayout: Boolean = false,
        val isNewLayoutVisible: Boolean = false
    )

    sealed class Input {
        data class MediaProjectionUpdated(val mediaProjection: MediaProjection?) : Input()
    }

    sealed class Output {
        data class RequestPermissionsOutput(val permissionsToRequest: Set<String>) : Output()
        data class RequestScreenCapture(val shouldRequest: Any) : Output()
        data class SettingsUpdated(val featureFlagsCollection: FeatureFlagsCollection) : Output()
    }
}