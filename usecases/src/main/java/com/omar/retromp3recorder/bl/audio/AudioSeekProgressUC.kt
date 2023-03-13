package com.omar.retromp3recorder.bl.audio

import com.omar.retromp3recorder.storage.repo.local.PlayerProgressRepo
import javax.inject.Inject

class AudioSeekProgressUC @Inject constructor(
    private val playerProgressRepo: PlayerProgressRepo
) {
      fun execute(position: Long) {
        playerProgressRepo.emit(PlayerProgressRepo.In.Seek(position))
    }
}
