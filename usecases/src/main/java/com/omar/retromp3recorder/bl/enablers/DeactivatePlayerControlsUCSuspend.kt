package com.omar.retromp3recorder.bl.enablers

import com.omar.retromp3recorder.domain.PlayerControls
import com.omar.retromp3recorder.storage.repo.local.PlayerControlsRepo
import com.omar.retromp3recorder.utils.domain.repo.first
import javax.inject.Inject

class DeactivatePlayerControlsUCSuspend @Inject constructor(
    private val playerControlsRepo: PlayerControlsRepo
) {
    suspend fun execute() {
        val (loop, range, reverse, speed) = playerControlsRepo.first()
        playerControlsRepo.emit(
            PlayerControls(
                loop.copy(isEnabled = false),
                range.copy(isVisible = false),
                reverse.copy(isEnabled = false),
                speed.copy(isEnabled = false)
            )
        )
    }
}
