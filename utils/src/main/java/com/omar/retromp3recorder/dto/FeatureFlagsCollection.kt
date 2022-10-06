package com.omar.retromp3recorder.dto

import androidx.annotation.Keep
import androidx.annotation.StringRes
import com.omar.retromp3recorder.app.utils.R

@Keep
enum class FeatureFlag(
    val featureLevel: FeatureLevel,
    @StringRes val friendlyName: Int,
    val isEnabledByDefault: Boolean
) {

    LogView(
        featureLevel = FeatureLevel.Production,
        friendlyName = R.string.feature_name_log_view,
        isEnabledByDefault = false
    ),
    KeepScreenOn(
        featureLevel = FeatureLevel.Production,
        friendlyName = R.string.feature_name_keep_screen_on,
        isEnabledByDefault = false

    ),
    ;

    val key: String = "FeatureFlag_${this.name}"

}

// Changes display container
@Keep
enum class FeatureLevel {
    Debug,
    Experimental,
    Production
}

data class FeatureFlagSetting(
    val isEnabled: Boolean,
)

data class FeatureFlagsCollection(
    val featuresMap: Map<FeatureFlag, FeatureFlagSetting>
) {
    fun isEnabled(featureFlag: FeatureFlag): Boolean {
        return featuresMap[featureFlag]?.isEnabled ?: false
    }
}
