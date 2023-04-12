package com.omar.retromp3recorder.bl.audio.effects

import com.omar.retromp3recorder.storage.repo.local.PlayerControlsRepo
import javax.inject.Inject

class SetPlaybackSpeed @Inject constructor(
    private val playerControlsRepo: PlayerControlsRepo
) {
    suspend fun execute(speed: Float) {
        val currentSettings = playerControlsRepo.first()
        val speedSettings = currentSettings.speedSettings.copy(speed = speed)
        playerControlsRepo.emit(currentSettings.copy(speedSettings = speedSettings))
    }
}

class SetPlaybackSpeedEnabledUC @Inject constructor(
    private val playerControlsRepo: PlayerControlsRepo
) {
    suspend fun execute(isEnabled: Boolean) {
        val currentSettings = playerControlsRepo.first()
        val speedSettings = currentSettings.speedSettings.copy(isEnabled = isEnabled)
        playerControlsRepo.emit(currentSettings.copy(speedSettings = speedSettings))
    }
}
