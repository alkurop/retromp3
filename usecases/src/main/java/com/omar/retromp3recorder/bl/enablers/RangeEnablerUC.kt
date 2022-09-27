package com.omar.retromp3recorder.bl.enablers

import com.omar.retromp3recorder.storage.repo.local.PlayerControlsRepo
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class RangeEnablerUC @Inject constructor(
    private val playerControlsRepo: PlayerControlsRepo
) {
    fun execute(isEnabled: Boolean): Completable = playerControlsRepo.takeOne().flatMapCompletable {
        Completable.fromAction {
            val updatedSettings = it.copy(range = it.range.copy(isVisible = isEnabled))
            playerControlsRepo.onNext(updatedSettings)
        }
    }
}
