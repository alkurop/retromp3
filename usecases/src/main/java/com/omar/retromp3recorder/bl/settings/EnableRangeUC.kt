package com.omar.retromp3recorder.bl.settings

import com.omar.retromp3recorder.storage.repo.PlayerFeaturesRepo
import com.omar.retromp3recorder.storage.repo.common.PlayerProgressRepo
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class EnableRangeUC @Inject constructor(
    private val playerFeaturesRepo: PlayerFeaturesRepo,
    private val playerProgressRepo: PlayerProgressRepo
) {
    fun execute() =
        playerFeaturesRepo.takeOne()
            .flatMapCompletable { features ->
                Completable.fromAction {
                    val isEnabled = features.range.isEnabled.not()
                    playerFeaturesRepo.onNext(features.copy(range = features.range.copy(isEnabled)))
                    playerProgressRepo.onNext(PlayerProgressRepo.In.RangeEnabled(isEnabled))
                }
            }
}
