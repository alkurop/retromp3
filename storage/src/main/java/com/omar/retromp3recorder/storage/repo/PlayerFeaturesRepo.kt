package com.omar.retromp3recorder.storage.repo

import com.omar.retromp3recorder.dto.PlayerFeatures
import com.omar.retromp3recorder.storage.repo.common.BehaviorSubjectRepo
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerFeaturesRepo @Inject constructor(
    private val featureFlagRepo: FeatureFlagRepo
) : BehaviorSubjectRepo<PlayerFeatures>() {
    override fun observe(): Observable<PlayerFeatures> {
        return Observable.zip(
            featureFlagRepo.observe(),
            super.observe()
        ) { flags, features ->
            val isRangeEnabled = flags.isEnabled(FeatureFlag.RangeControl)
            features.copy(range = features.range.copy(isEnabled = isRangeEnabled))
        }
    }
}
