package com.omar.retromp3recorder.bl.audio.effects

import com.omar.retromp3recorder.storage.repo.local.PlayerControlsRepo
import javax.inject.Inject

class PlaybackSpeedSetUC @Inject constructor(
    private val playerControlsRepo: PlayerControlsRepo
) {
    suspend fun execute(speed: Float) {
        val currentSettings = playerControlsRepo.first()
        val speedSettings = currentSettings.speedSettings.copy(speed = speed)
        playerControlsRepo.emit(currentSettings.copy(speedSettings = speedSettings))
    }
}

class PlaybackSpeedEnabledUC @Inject constructor(
    private val playerControlsRepo: PlayerControlsRepo
) {
    suspend fun execute() {
        val currentSettings = playerControlsRepo.first()
        val isEnabled = currentSettings.speedSettings.isEnabled.not()
        val speedSettings = currentSettings.speedSettings.copy(isEnabled = isEnabled)
        playerControlsRepo.emit(currentSettings.copy(speedSettings = speedSettings))
    }
}

class PlaybackSpeedVisibleUC @Inject constructor(
    private val playerControlsRepo: PlayerControlsRepo
) {
    suspend fun execute(isVisible: Boolean) {
        val currentSettings = playerControlsRepo.first()
        val speedSettings = currentSettings.speedSettings.copy(isVisible = isVisible)
        playerControlsRepo.emit(currentSettings.copy(speedSettings = speedSettings))
    }
}

