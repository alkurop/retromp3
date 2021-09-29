package com.omar.retromp3recorder.bl.settings

import android.content.SharedPreferences
import com.omar.retromp3recorder.storage.repo.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class FeatureMapLoadUC @Inject constructor(
    private val featureFlagRepo: FeatureFlagRepo,
    private val sharedPreferences: SharedPreferences
) {
    fun execute(): Completable = Completable
        .fromAction {
            val featuresMap = FeatureFlag
                .values()
                .map { featureFlag ->
                    val key = featureFlag.key
                    val isEnabled = sharedPreferences.getBoolean(
                        key,
                        featureFlag.isDefaultEnabled
                    )
                    featureFlag to FeatureFlagSetting(isEnabledOverride = isEnabled)
                }
                .toMap()
            featureFlagRepo.onNext(FeatureFlagsCollection(featuresMap))
        }
        .subscribeOn(Schedulers.io())
}