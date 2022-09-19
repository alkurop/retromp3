package com.omar.retromp3recorder.bl.settings

import com.omar.retromp3recorder.storage.repo.PlayerFeaturesRepo
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class EnableRangeUC @Inject constructor(
    private val playerFeaturesRepo: PlayerFeaturesRepo
) {
    fun execute() =
        playerFeaturesRepo.takeOne()
            .flatMapCompletable { features ->
                Completable.fromAction {
                    playerFeaturesRepo.onNext(features.copy(range = features.range.copy(features.range.isEnabled.not())))
                }
            }
}
