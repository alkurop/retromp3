package com.omar.retromp3recorder.app.main

import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.domain.FeatureFlagsCollection

object MainActivityContract {
    data class State(
        val shouldKeepScreenOn: Boolean = false,
        val toast: Shell<String> = Shell.empty()
    )

    sealed interface Output {
        data class SettingsUpdated(val featureFlagsCollection: FeatureFlagsCollection) : Output
        data class ShowToast(val text: String) : Output
    }
}
