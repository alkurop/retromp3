package com.omar.retromp3recorder.app.screens.home.components.speedbar

import com.omar.retromp3recorder.bl.audio.progress.JoinedProgressMapper
import com.omar.retromp3recorder.domain.JoinedProgress
import com.omar.retromp3recorder.storage.repo.local.PlayerControlsRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class SpeedBarStateMapper @Inject constructor(
    private val joinedProgressRepo: JoinedProgressMapper,
    private val playerControlsRepo: PlayerControlsRepo
) {
    fun flow(): Flow<SpeedBarContract.State> {
        return combine(
            joinedProgressRepo.flow(),
            playerControlsRepo.flow(),
        ) { progress, controls ->
            when (progress) {
                is JoinedProgress.PlayerProgressShown -> {
                    val range = progress.progress.range
                    if (range.settings.isVisible) {
                        val duration = progress.progress.duration
                        SpeedBarContract.State.Visible(controls.speedSettings)
                    } else {
                        SpeedBarContract.State.Hidden
                    }
                }
                else -> SpeedBarContract.State.Hidden

            }
        }
    }
}
