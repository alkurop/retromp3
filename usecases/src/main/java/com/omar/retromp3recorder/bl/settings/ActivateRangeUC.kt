package com.omar.retromp3recorder.bl.settings

import com.omar.retromp3recorder.storage.repo.local.PlayerFeaturesRepo
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class ActivateRangeUC @Inject constructor(
    private val playerFeaturesRepo: PlayerFeaturesRepo,
) {
    fun execute() =
        playerFeaturesRepo.takeOne()
            .flatMapCompletable { features ->
                Completable.fromAction {
                    val isActive = features.range.isActive.not()
                    playerFeaturesRepo.onNext(
                        features.copy(
                            range = features.range.copy(
                                isActive = isActive
                            )
                        )
                    )
                }
            }
}
