package com.omar.retromp3recorder.bl.audio

import com.omar.retromp3recorder.storage.repo.common.PlayerProgressRepo
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class AudioSeekProgressUC @Inject constructor(
    private val playerProgressRepo: PlayerProgressRepo
) {
    fun execute(position: Long): Completable =
        Completable.fromAction {
            playerProgressRepo.onNext(PlayerProgressRepo.In.Seek(position))
        }
}