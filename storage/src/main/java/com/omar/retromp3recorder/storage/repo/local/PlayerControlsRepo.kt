package com.omar.retromp3recorder.storage.repo.local

import com.omar.retromp3recorder.dto.FeatureFlag
import com.omar.retromp3recorder.dto.FeatureFlagsCollection
import com.omar.retromp3recorder.dto.PlayerFeatures
import com.omar.retromp3recorder.storage.repo.common.BehaviorSubjectRepo
import com.omar.retromp3recorder.storage.repo.global.FeatureFlagRepo
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerControlsRepo @Inject constructor(
    private val featureFlagRepo: FeatureFlagRepo
) : BehaviorSubjectRepo<PlayerFeatures>(PlayerFeatures()) {
    override fun observe(): Observable<PlayerFeatures> {
        return Observable.combineLatest(
            featureFlagRepo.observe(),
            super.observe(), reducer
        )
    }
}

private val reducer: (FeatureFlagsCollection, PlayerFeatures) -> PlayerFeatures =
    { flags, features ->
        val isRangeAllowed = flags.isEnabled(FeatureFlag.RangeControl)
        val range = PlayerFeatures.Range(
            isVisible = isRangeAllowed,
            isActive = if (isRangeAllowed) features.range.isActive else false
        )
        features.copy(range = range)
    }
