package com.omar.retromp3recorder.storage.repo

import com.omar.retromp3recorder.storage.BuildConfig

enum class FeatureFlag(val featureLevel: FeatureLevel) {
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
