package com.omar.retromp3recorder.app.ui.rangebar

import com.omar.retromp3recorder.dto.JoinedProgress
import com.omar.retromp3recorder.storage.repo.FeatureFlag
import com.omar.retromp3recorder.storage.repo.FeatureFlagRepo
import com.omar.retromp3recorder.storage.repo.JoinedProgressRepo
import com.omar.retromp3recorder.storage.repo.PlayerRangeRepo
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class RangeBarStateMapper @Inject constructor(
    private val joinedProgressRepo: JoinedProgressRepo,
    private val featureFlagRepo: FeatureFlagRepo,
    private val rangeRepo: PlayerRangeRepo,
) {
    fun observe(): Observable<RangeBarView.State> {
        return Observable.combineLatest(
            joinedProgressRepo.observe(),
            featureFlagRepo.observe(),
            rangeRepo.observe(),
            { progress, features, _ ->
                val isFlag = FeatureFlag.RangeControl.isEnabled(features)
                when {
                    isFlag.not() ||
                            progress is JoinedProgress.Hidden ||
                            progress is JoinedProgress.RecorderProgressShown -> RangeBarView.State.Hidden
                    progress is JoinedProgress.PlayerProgressShown -> {
                        RangeBarView.State.Hidden
                    }
                    else -> RangeBarView.State.Hidden
                }
            })
    }
}