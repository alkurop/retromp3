package com.omar.retromp3recorder.app.ui.rangebar

import com.omar.retromp3recorder.dto.JoinedProgress
import com.omar.retromp3recorder.storage.repo.JoinedProgressRepo
import com.omar.retromp3recorder.storage.repo.PlayerFeaturesRepo
import com.omar.retromp3recorder.storage.repo.RangeBarResetBus
import com.omar.retromp3recorder.utils.toFromToMillis
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class RangeBarStateMapper @Inject constructor(
    private val joinedProgressRepo: JoinedProgressRepo,
    private val playerFeaturesRepo: PlayerFeaturesRepo,
    private val rangeBarResetBus: RangeBarResetBus,

    ) {
    fun observe(): Observable<RangeBarView.State> {
        return Observable.combineLatest(
            joinedProgressRepo.observe(),
            playerFeaturesRepo.observe(),
            rangeBarResetBus.observe(),
        ) { progress, features, reset ->
            when {
                progress is JoinedProgress.PlayerProgressShown && features.range.isVisible -> {
                    val duration = progress.progress.duration
                    val range = progress.progress.range
                    RangeBarView.State.Visible(
                        range = range,
                        fromToMillis = range.toFromToMillis(duration),
                        isActive = features.range.isActive,
                        reset
                    )
                }
                else -> RangeBarView.State.Hidden
            }
        }
    }
}
