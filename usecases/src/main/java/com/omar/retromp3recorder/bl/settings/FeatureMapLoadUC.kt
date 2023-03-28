package com.omar.retromp3recorder.bl.settings

import android.content.SharedPreferences
import com.omar.retromp3recorder.domain.FeatureFlag
import com.omar.retromp3recorder.domain.FeatureFlagSetting
import com.omar.retromp3recorder.domain.FeatureFlagsCollection
import com.omar.retromp3recorder.storage.repo.global.FeatureFlagRepo
import javax.inject.Inject

class FeatureMapLoadUC @Inject constructor(
    private val featureFlagRepo: FeatureFlagRepo,
    private val sharedPreferences: SharedPreferences
) {
    suspend fun execute() {
        val featuresMap = FeatureFlag
            .values().associateWith { featureFlag ->
                val key = featureFlag.key
                val isEnabled = sharedPreferences.getBoolean(
                    key, featureFlag.isEnabledByDefault
                )
                FeatureFlagSetting(isEnabled = isEnabled)
            }
        featureFlagRepo.emit(
            FeatureFlagsCollection(
                featuresMap
            )
        )
    }
}
