package com.omar.retromp3recorder.app.screens.main.components.rangebar

import com.omar.retromp3recorder.bl.audio.JoinedProgressMapper
import com.omar.retromp3recorder.domain.JoinedProgress
import com.omar.retromp3recorder.storage.repo.local.PlayerControlsRepo
import com.omar.retromp3recorder.storage.repo.local.RangeBarResetBus
import com.omar.retromp3recorder.utils.domain.toFromToMillis
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.rx3.asFlow
import javax.inject.Inject

class RangeBarStateMapperFlow @Inject constructor(
    private val joinedProgressRepo: JoinedProgressMapper,
    private val playerControlsRepo: PlayerControlsRepo,
    private val rangeBarResetBus: RangeBarResetBus,
) {
    fun flow(): Flow<RangeBarView.State> {
        return combine(
            joinedProgressRepo.observe().asFlow(),
            playerControlsRepo.flow(),
            rangeBarResetBus.flow(),
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
