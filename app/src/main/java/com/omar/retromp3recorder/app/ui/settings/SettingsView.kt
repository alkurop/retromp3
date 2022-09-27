package com.omar.retromp3recorder.app.ui.settings

import com.omar.retromp3recorder.dto.FeatureFlag
import com.omar.retromp3recorder.dto.FeatureFlagSetting
import com.omar.retromp3recorder.dto.FeatureFlagsCollection

class SettingsView {
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
