package com.omar.retromp3recorder.app.screens.home.components.rangebar

import com.omar.retromp3recorder.bl.audio.progress.JoinedProgressMapper
import com.omar.retromp3recorder.domain.JoinedProgress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RangeBarVisibilityMapper @Inject constructor(
    private val joinedProgressRepo: JoinedProgressMapper,
) {
    fun flow(): Flow<Boolean> {
        return joinedProgressRepo.flow().map { progress ->
            if (progress is JoinedProgress.PlayerProgressShown) {
                val range = progress.progress.range
                range.settings.isVisible
            } else false
        }
    }
}
