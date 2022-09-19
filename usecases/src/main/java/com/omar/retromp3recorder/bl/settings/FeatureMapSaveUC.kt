package com.omar.retromp3recorder.bl.settings

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.omar.retromp3recorder.storage.repo.FeatureFlag
import com.omar.retromp3recorder.storage.repo.FeatureFlagRepo
import com.omar.retromp3recorder.storage.repo.FeatureFlagSetting
import com.omar.retromp3recorder.storage.repo.FeatureFlagsCollection
import com.omar.retromp3recorder.utils.takeOne
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class FeatureMapSaveUC @Inject constructor(
    private val featureFlagRepo: FeatureFlagRepo,
    private val sharedPreferences: SharedPreferences
) {
    @SuppressLint("ApplySharedPref")
    fun execute(featureFlag: FeatureFlag, featureFlagSetting: FeatureFlagSetting): Completable =
        featureFlagRepo.observe().takeOne().flatMapCompletable { features ->
            Completable.fromAction {
                val intermediate = features.featuresMap.toMutableMap()
                intermediate[featureFlag] = featureFlagSetting

                featureFlagRepo.onNext(FeatureFlagsCollection(intermediate.toMap()))
                sharedPreferences.edit()
                    .putBoolean(featureFlag.key, featureFlagSetting.isEnabled)
                    .commit()
            }
        }
}
