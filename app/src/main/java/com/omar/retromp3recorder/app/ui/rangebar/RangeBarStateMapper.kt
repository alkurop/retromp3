package com.omar.retromp3recorder.app.ui.rangebar

import com.omar.retromp3recorder.dto.JoinedProgress
import com.omar.retromp3recorder.storage.repo.JoinedProgressRepo
import com.omar.retromp3recorder.storage.repo.PlayerFeaturesRepo
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class RangeBarStateMapper @Inject constructor(
    private val joinedProgressRepo: JoinedProgressRepo,
    private val playerFeaturesRepo: PlayerFeaturesRepo,
) {
    fun observe(): Observable<RangeBarView.State> {
        return Observable.combineLatest(
            joinedProgressRepo.observe(),
            playerFeaturesRepo.observe(),
        ) { progress, features ->
            when {
                progress is JoinedProgress.PlayerProgressShown && features.range.isVisible -> {
                    val duration = progress.progress.duration
                    val range = progress.progress.range
                    val rangeMultiplier = if (range.max == 0) 1 else duration / range.max
                    val fromMillis = if (range.from == 0) 0 else range.from * rangeMultiplier
                    val toMillis = if (range.to == 0) 0 else range.to * rangeMultiplier
                    RangeBarView.State.Visible(
                        range = range,
                        fromMillis = fromMillis,
                        toMillis = toMillis,
                        features.range.isActive
                    )
                }
                else -> RangeBarView.State.Hidden
            }
        }
    }
}
