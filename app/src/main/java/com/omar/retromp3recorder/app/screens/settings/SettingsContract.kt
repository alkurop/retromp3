package com.omar.retromp3recorder.app.screens.settings

import androidx.compose.runtime.Immutable
import com.omar.retromp3recorder.domain.FeatureFlag
import com.omar.retromp3recorder.domain.FeatureFlagSetting
import com.omar.retromp3recorder.domain.FeatureFlagsCollection

class SettingsContract {

    @Immutable
    data class State(
        val featureFlagsCollection: FeatureFlagsCollection? = null
    )

    sealed class Input {
        data class FlagSettingChanged(
            val flag: FeatureFlag,
            val setting: FeatureFlagSetting
        ): Input()
    }

    sealed class Output {
        data class FlagsCollectionUpdate(
            val featureFlagsCollection: FeatureFlagsCollection
        ): Output()
    }
}
