package com.omar.retromp3recorder.storage.repo

import com.omar.retromp3recorder.dto.PlayerFeatures
import com.omar.retromp3recorder.dto.RangeFeature
import com.omar.retromp3recorder.storage.repo.common.BehaviorSubjectRepo
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerFeaturesRepo @Inject constructor(
    private val featureFlagRepo: FeatureFlagRepo
) : BehaviorSubjectRepo<PlayerFeatures>(PlayerFeatures()) {
    override fun observe(): Observable<PlayerFeatures> {
        return Observable.combineLatest(
            featureFlagRepo.observe(),
            super.observe()
        ) { flags, features ->
            val isRangeAllowed = flags.isEnabled(FeatureFlag.RangeControl)
            val range = RangeFeature(
                isVisible = isRangeAllowed,
                isActive = if (isRangeAllowed) features.range.isActive else false
            )
            features.copy(range = range)
        }
    }
}
