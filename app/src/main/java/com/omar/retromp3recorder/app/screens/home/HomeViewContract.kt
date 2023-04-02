package com.omar.retromp3recorder.app.screens.home

import android.media.projection.MediaProjection
import androidx.compose.runtime.Immutable
import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.domain.FeatureFlagsCollection

object HomeViewContract {
    @Immutable
    data class State(
        val requestForScreenCapture: Shell<Any> = Shell.empty(),
        val isLogViewEnabled: Boolean = false,
    )

    sealed class Input {
        data class MediaProjectionUpdated(val mediaProjection: MediaProjection?) : Input()
    }

    sealed class Output {
        data class RequestScreenCapture(val shouldRequest: Shell<Any>) : Output()
        data class SettingsUpdated(val featureFlagsCollection: FeatureFlagsCollection) : Output()
    }
}
