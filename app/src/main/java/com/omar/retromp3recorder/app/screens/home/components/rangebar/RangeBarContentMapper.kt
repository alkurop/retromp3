package com.omar.retromp3recorder.app.screens.home.components.rangebar

import com.omar.retromp3recorder.bl.audio.progress.JoinedProgressMapper
import com.omar.retromp3recorder.domain.JoinedProgress
import com.omar.retromp3recorder.storage.repo.local.RangeBarResetBus
import com.omar.retromp3recorder.utils.domain.toFromToMillis
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject

class RangeBarContentMapper @Inject constructor(
    private val joinedProgressRepo: JoinedProgressMapper,
    private val rangeBarResetBus: RangeBarResetBus,
) {
    fun flow(): Flow<RangeBarContract.BarContent> {
        return combine(
            joinedProgressRepo.flow(),
            rangeBarResetBus.flow(),
        ) { progress, reset ->
            if (progress is JoinedProgress.PlayerProgressShown) {
                val duration = progress.progress.duration
                val range = progress.progress.range
                RangeBarContract.BarContent(
                    range = range,
                    fromToMillis = range.toFromToMillis(duration),
                    isActive = range.settings.isActive,
                    reset
                )
            } else null
        }.filterNotNull()
    }
}
