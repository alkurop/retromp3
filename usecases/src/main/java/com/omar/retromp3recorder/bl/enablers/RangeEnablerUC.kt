package com.omar.retromp3recorder.bl.enablers

import com.omar.retromp3recorder.storage.repo.local.PlayerControlsRepo
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class RangeEnablerUC @Inject constructor(
    private val playerControlsRepo: PlayerControlsRepo
) {
    fun execute(isEnabled: Boolean): Completable =
        playerControlsRepo.takeSingle()
            .flatMapCompletable {
                Completable.fromAction {
                    val range = it.rangeSettings.copy(isVisible = isEnabled, isActive = false)
                    val updatedSettings = it.copy(rangeSettings = range)
                    playerControlsRepo.tryNext(updatedSettings)
                }
            }
}
