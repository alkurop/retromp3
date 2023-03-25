package com.omar.retromp3recorder.domain


data class FeatureFlagsCollection(
    val featuresMap: Map<FeatureFlag, FeatureFlagSetting>
) {
    fun isEnabled(featureFlag: FeatureFlag): Boolean {
        return featuresMap[featureFlag]?.isEnabled ?: false
    }
}
