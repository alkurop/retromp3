package com.omar.retromp3recorder.bl.audio

import com.omar.retromp3recorder.dto.PlayerRange
import com.omar.retromp3recorder.storage.repo.PlayerRangeRepo
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class UpdatePlayerRangeUC @Inject constructor(
    private val playerRangeRepo: PlayerRangeRepo
) {
    fun execute(range: PlayerRange) = Completable.fromAction {
        playerRangeRepo.onNext(range)
    }
}