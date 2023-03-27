package com.omar.retromp3recorder.bl.settings

import com.omar.retromp3recorder.storage.repo.local.PlayerControlsRepo
import javax.inject.Inject

class ActivateRangeUC @Inject constructor(
    private val playerControlsRepo: PlayerControlsRepo,
) {
    suspend fun execute() {
        val features = playerControlsRepo.first()
        val isActive = features.rangeSettings.isActive.not()
        playerControlsRepo.emit(
            features.copy(
                rangeSettings = features.rangeSettings.copy(
                    isActive = isActive
                )
            )
        )
    }
}
