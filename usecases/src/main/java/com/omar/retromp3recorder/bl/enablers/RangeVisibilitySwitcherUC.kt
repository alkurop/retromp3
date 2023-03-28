package com.omar.retromp3recorder.bl.enablers

import com.omar.retromp3recorder.storage.repo.local.PlayerControlsRepo
import javax.inject.Inject

class RangeVisibilitySwitcherUC @Inject constructor(
    private val playerControlsRepo: PlayerControlsRepo
) {
    suspend fun execute(isVisible: Boolean) {
        val settings = playerControlsRepo.first()
        val range = settings.rangeSettings.copy(isVisible = isVisible)
        val updatedSettings = settings.copy(rangeSettings = range)
        playerControlsRepo.emit(updatedSettings)
    }
}
