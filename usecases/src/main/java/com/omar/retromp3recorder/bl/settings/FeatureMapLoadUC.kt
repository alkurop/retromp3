package com.omar.retromp3recorder.bl.settings

import android.content.SharedPreferences
import com.omar.retromp3recorder.storage.repo.FeatureFlag
import com.omar.retromp3recorder.storage.repo.FeatureFlagRepo
import com.omar.retromp3recorder.storage.repo.FeatureFlagSetting
import com.omar.retromp3recorder.storage.repo.FeatureFlagsCollection
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
                        key, featureFlag.isEnabledByDefault
                    )
                    featureFlag to FeatureFlagSetting(isEnabled = isEnabled)
                }
                .toMap()
            featureFlagRepo.onNext(FeatureFlagsCollection(featuresMap))
        }
        .subscribeOn(Schedulers.io())
}
