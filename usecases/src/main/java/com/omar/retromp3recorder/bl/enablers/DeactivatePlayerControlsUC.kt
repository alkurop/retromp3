package com.omar.retromp3recorder.bl.enablers

import com.omar.retromp3recorder.domain.PlayerControls
import com.omar.retromp3recorder.storage.repo.local.PlayerControlsRepo
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class DeactivatePlayerControlsUC @Inject constructor(
    private val playerControlsRepo: PlayerControlsRepo
) {
    fun execute(): Completable = playerControlsRepo.takeSingle()
        .flatMapCompletable { (loop, range, reverse, speed) ->
            Completable.fromAction {
                playerControlsRepo.onNext(
                    PlayerControls(
                        loop.copy(isEnabled = false),
                        range.copy(isVisible = false),
                        reverse.copy(isEnabled = false),
                        speed.copy(isEnabled = false)
                    )
                )
            }
        }
}
