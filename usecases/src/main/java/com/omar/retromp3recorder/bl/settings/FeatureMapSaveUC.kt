package com.omar.retromp3recorder.bl.settings

import android.content.SharedPreferences
import com.omar.retromp3recorder.domain.FeatureFlag
import com.omar.retromp3recorder.domain.FeatureFlagSetting
import com.omar.retromp3recorder.domain.FeatureFlagsCollection
import com.omar.retromp3recorder.storage.repo.global.FeatureFlagRepo
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class FeatureMapSaveUC @Inject constructor(
    private val featureFlagRepo: FeatureFlagRepo,
    private val sharedPreferences: SharedPreferences
) {
    suspend fun execute(featureFlag: FeatureFlag, featureFlagSetting: FeatureFlagSetting) {
        val features = featureFlagRepo.flow().first()
        val intermediate = features.featuresMap.toMutableMap()
        intermediate[featureFlag] = featureFlagSetting

        featureFlagRepo.onNext(
            FeatureFlagsCollection(
                intermediate.toMap()
            )
        )
        sharedPreferences.edit().putBoolean(featureFlag.key, featureFlagSetting.isEnabled).apply()
    }
}
