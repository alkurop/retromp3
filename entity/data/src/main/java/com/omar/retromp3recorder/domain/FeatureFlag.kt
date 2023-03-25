package com.omar.retromp3recorder.domain


enum class FeatureLevel {
    Debug,
    Experimental,
    Production
}

data class FeatureFlagSetting(
    val isEnabled: Boolean,
)


enum class FeatureFlag(
    val featureLevel: FeatureLevel,
    val isEnabledByDefault: Boolean
) {

    LogView(
        featureLevel = FeatureLevel.Production,
        isEnabledByDefault = false
    ),
    KeepScreenOn(
        featureLevel = FeatureLevel.Production,
        isEnabledByDefault = false

    ),
    ;

    val key: String = "FeatureFlag_${this.name}"

}
