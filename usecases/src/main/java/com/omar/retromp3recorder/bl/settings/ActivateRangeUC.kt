package com.omar.retromp3recorder.bl.settings

import com.omar.retromp3recorder.storage.repo.local.PlayerControlsRepo
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class ActivateRangeUC @Inject constructor(
    private val playerControlsRepo: PlayerControlsRepo,
) {
    fun execute() =
        playerControlsRepo.takeOne()
            .flatMapCompletable { features ->
                Completable.fromAction {
                    val isActive = features.range.isActive.not()
                    playerControlsRepo.onNext(
                        features.copy(
                            range = features.range.copy(
                                isActive = isActive
                            )
                        )
                    )
                }
            }
}
