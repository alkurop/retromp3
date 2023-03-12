package com.omar.retromp3recorder.bl.audio

import com.omar.retromp3recorder.domain.PlayerRange
import com.omar.retromp3recorder.storage.repo.local.PlayerProgressRepo
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class UpdatePlayerRangeUC @Inject constructor(
    private val playerProgressRepo: PlayerProgressRepo
) {
    fun execute(range: PlayerRange): Completable =
        Completable.fromAction {
            playerProgressRepo.onNext(PlayerProgressRepo.In.Range(range))
        }
}
