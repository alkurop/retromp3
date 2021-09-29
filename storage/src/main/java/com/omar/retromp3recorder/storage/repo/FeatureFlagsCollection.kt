package com.omar.retromp3recorder.storage.repo

import androidx.annotation.StringRes
import com.omar.retromp3recorder.storage.R

enum class FeatureFlag(
    val featureLevel: FeatureLevel,
    val isDefaultEnabled: Boolean = false,
    @StringRes val friendlyName: Int
) {
    DebugWindow(
        featureLevel = FeatureLevel.Debug,
        friendlyName = R.string.feature_name_debug_window
    ),
    ;

    val key: String = "FeatureFlag_${this.name}"
}

enum class FeatureLevel {
    Debug,
    Experimental,
    Production
}

data class FeatureFlagSetting(
    val isEnabledOverride: Boolean = false,
    val isManuallySet: Boolean = false
) {
    @Suppress("UNUSED")
            /** Im not going to use this
             * This is more like a doc on how the Feature Settings are going to be used*/
    fun isEnabled(featureFlag: FeatureFlag): Boolean {
        return when {
            this.isManuallySet -> this.isEnabledOverride
            else -> featureFlag.isDefaultEnabled
        }
    }
}

data class FeatureFlagsCollection(
    val featuresMap: Map<FeatureFlag, FeatureFlagSetting> = FeatureFlag
        .values()
        .map { it to FeatureFlagSetting() }
        .toMap()
)
