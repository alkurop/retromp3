package com.omar.retromp3recorder.app.ui.main

import android.media.projection.MediaProjection
import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.domain.FeatureFlagsCollection

object MainViewContract {
    data class State(
        val requestForPermissions: Shell<Set<String>> = Shell.empty(),
        val requestForScreenCapture: Shell<Any> = Shell.empty(),
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
