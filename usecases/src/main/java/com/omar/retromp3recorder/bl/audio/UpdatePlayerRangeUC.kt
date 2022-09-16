package com.omar.retromp3recorder.bl.audio

import com.omar.retromp3recorder.dto.PlayerRange
import com.omar.retromp3recorder.storage.repo.JoinedProgressRepo
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class UpdatePlayerRangeUC @Inject constructor(
    private val joinedProgressRepo: JoinedProgressRepo
) {
    fun execute(range: PlayerRange) = Completable.fromAction {
        //todo update here
//        playerRangeRepo.onNext(range)
    }
}
