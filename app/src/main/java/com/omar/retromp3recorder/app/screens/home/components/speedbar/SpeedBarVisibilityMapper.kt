package com.omar.retromp3recorder.app.screens.home.components.speedbar

import com.omar.retromp3recorder.storage.repo.local.PlayerControlsRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SpeedBarVisibilityMapper @Inject constructor(
    private val playerControls: PlayerControlsRepo
) {
    fun flow(): Flow<Boolean> {
        return playerControls.flow().map { controls ->
            controls.speedSettings.isVisible
        }
    }
}
