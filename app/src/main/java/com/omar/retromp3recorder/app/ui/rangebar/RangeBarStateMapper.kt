package com.omar.retromp3recorder.app.ui.rangebar

import com.omar.retromp3recorder.dto.JoinedProgress
import com.omar.retromp3recorder.dto.PlayerRange.Companion.MAX_RANGE
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
            rangeRepo.observe()
        ) { progress, features, range ->
            val isFlag = FeatureFlag.RangeControl.isEnabled(features)
            when {
                isFlag && progress is JoinedProgress.PlayerProgressShown -> {
                    val duration = progress.progress.duration
                    val fromMillis = if (range.from == 0) 0 else duration * MAX_RANGE / range.from
                    val toMillis = duration * MAX_RANGE / range.to
                    RangeBarView.State.Visible(
                        range = range,
                        fromMillis = fromMillis,
                        toMillis = toMillis
                    )
                }
                else -> RangeBarView.State.Hidden
            }
        }
    }
}