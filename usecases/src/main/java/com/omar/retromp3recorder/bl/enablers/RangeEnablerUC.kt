package com.omar.retromp3recorder.bl.enablers

import com.omar.retromp3recorder.storage.repo.local.PlayerControlsRepo
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class RangeEnablerUC @Inject constructor(
    private val playerControlsRepo: PlayerControlsRepo
) {
    fun execute(isEnabled: Boolean): Completable = playerControlsRepo.takeOne().flatMapCompletable {
        Completable.fromAction {
            val range = it.range.copy(isVisible = isEnabled, isActive = false)
            val updatedSettings = it.copy(range = range)
            playerControlsRepo.onNext(updatedSettings)
        }
    }
}
