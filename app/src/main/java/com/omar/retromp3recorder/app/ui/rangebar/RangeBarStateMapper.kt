package com.omar.retromp3recorder.app.ui.rangebar

import com.omar.retromp3recorder.bl.audio.JoinedProgressMapper
import com.omar.retromp3recorder.domain.JoinedProgress
import com.omar.retromp3recorder.storage.repo.local.PlayerControlsRepo
import com.omar.retromp3recorder.storage.repo.local.RangeBarResetBus
import com.omar.retromp3recorder.utils.domain.toFromToMillis
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class RangeBarStateMapper @Inject constructor(
    private val joinedProgressRepo: JoinedProgressMapper,
    private val playerControlsRepo: PlayerControlsRepo,
    private val rangeBarResetBus: RangeBarResetBus,

    ) {
    fun observe(): Observable<RangeBarView.State> {
        return Observable.combineLatest(
            joinedProgressRepo.observe(),
            playerControlsRepo.observe(),
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
