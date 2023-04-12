package com.omar.retromp3recorder.app.screens.home.components.rangebar

import com.omar.retromp3recorder.bl.audio.progress.JoinedProgressMapper
import com.omar.retromp3recorder.domain.JoinedProgress
import com.omar.retromp3recorder.storage.repo.local.RangeBarResetBus
import com.omar.retromp3recorder.utils.domain.toFromToMillis
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class RangeBarStateMapper @Inject constructor(
    private val joinedProgressRepo: JoinedProgressMapper,
    private val rangeBarResetBus: RangeBarResetBus,
) {
    fun flow(): Flow<RangeBarView.State> {
        return combine(
            joinedProgressRepo.flow(),
            rangeBarResetBus.flow(),
        ) { progress, reset ->
            when (progress) {
                is JoinedProgress.PlayerProgressShown -> {
                    val range = progress.progress.range
                    if (range.settings.isVisible) {
                        val duration = progress.progress.duration
                        RangeBarView.State.Visible(
                            range = range,
                            fromToMillis = range.toFromToMillis(duration),
                            isActive = range.settings.isActive,
                            reset
                        )
                    } else {
                        RangeBarView.State.Hidden
                    }
                }
                else -> RangeBarView.State.Hidden
            }
        }
    }
}
