package com.omar.retromp3recorder.storage.repo

import androidx.annotation.StringRes
import com.omar.retromp3recorder.storage.BuildConfig
import com.omar.retromp3recorder.storage.R

enum class FeatureFlag(
    val featureLevel: FeatureLevel = FeatureLevel.Debug,
    val isSwitchable: Boolean = true,
    @StringRes val friendlyName: Int
) {
    DebugWindow(
        friendlyName = R.string.feature_name_debug_window
    )
    ;

    val key: String = "FeatureFlag_${this.name}"
}

enum class FeatureLevel {
    Debug,
    Experimental,
    Production
}

data class FeatureFlagSetting(
    val isEnabled: Boolean = false,
    val isManuallySet: Boolean = false
) {
}

data class FeatureFlagsCollection(
    val featuresMap: Map<FeatureFlag, FeatureFlagSetting> = FeatureFlag
        .values()
        .map { it to FeatureFlagSetting() }
        .toMap()
) {

    @Suppress("UNUSED")
            /** Im not going to use this
             * This is more like a doc on how the Feature Settings are going to be used*/
    fun isEnabled(featureFlag: FeatureFlag): Boolean {
        val setting = featuresMap[featureFlag]!!
        return when {
            setting.isManuallySet -> setting.isEnabled
            featureFlag.featureLevel == FeatureLevel.Debug -> BuildConfig.DEBUG
            featureFlag.featureLevel == FeatureLevel.Experimental -> false
            featureFlag.featureLevel == FeatureLevel.Production -> true
            //impossible usecase
            else -> setting.isEnabled
        }
    }

}
