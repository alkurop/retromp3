package com.omar.retromp3recorder.app.ui.rangebar

import com.omar.retromp3recorder.storage.repo.FeatureFlag
import com.omar.retromp3recorder.storage.repo.FeatureFlagRepo
import com.omar.retromp3recorder.storage.repo.JoinedProgressRepo
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class RangeBarStateMapper @Inject constructor(
    private val joinedProgressRepo: JoinedProgressRepo,
    private val featureFlagRepo: FeatureFlagRepo,
) {
    fun observe(): Observable<RangeBarView.State> {
        return Observable.combineLatest(
            joinedProgressRepo.observe(),
            featureFlagRepo.observe(),
        ) { progress, features ->
            val isFlag = FeatureFlag.RangeControl.isEnabled(features)
            when {
                //todo update here
//                isFlag && progress is JoinedProgress.PlayerProgressShown -> {
//                    val duration = progress.progress.duration
//                    val rangeMultiplier = if (range.max == 0) 1 else duration / range.max
//                    val fromMillis = if (range.from == 0) 0 else range.from * rangeMultiplier
//                    val toMillis = if (range.to == 0) 0 else range.to * rangeMultiplier
//                    RangeBarView.State.Visible(
//                        range = range,
//                        fromMillis = fromMillis,
//                        toMillis = toMillis
//                    )
//                }
//
//                    val isFlag = FeatureFlag.RangeControl.isEnabled(features)
//                when {
//                    isFlag.not() ||
//                            progress is JoinedProgress.Hidden ||
//                            progress is JoinedProgress.RecorderProgressShown -> RangeBarView.State.Hidden
//                    progress is JoinedProgress.PlayerProgressShown -> {
//                        RangeBarView.State.Hidden
//                    }
//                    else -> RangeBarView.State.Hidden
//                }
//            })
                else -> RangeBarView.State.Hidden
            }
        }
    }
}
