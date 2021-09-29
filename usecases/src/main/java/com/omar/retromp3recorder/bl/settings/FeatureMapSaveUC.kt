package com.omar.retromp3recorder.bl.settings

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.omar.retromp3recorder.storage.repo.FeatureFlag
import com.omar.retromp3recorder.storage.repo.FeatureFlagRepo
import com.omar.retromp3recorder.storage.repo.FeatureFlagSetting
import com.omar.retromp3recorder.utils.takeOne
import com.omar.retromp3recorder.utils.takeOneObservable
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
                val newMap = features.copy(featuresMap = intermediate.toMap())
                featureFlagRepo.onNext(newMap)
                if (featureFlagSetting.isManuallySet) {
                    sharedPreferences.edit().putBoolean(featureFlag.key, featureFlagSetting.isEnabledOverride)
                        .commit()
                }
            }
        }
}