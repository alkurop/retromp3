package com.omar.retromp3recorder.storage.repo.local

import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.audioplayer.progressFlow
import com.omar.retromp3recorder.domain.PlayerProgress
import com.omar.retromp3recorder.domain.PlayerRange
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlayerProgressMapperFlow @Inject constructor(
    private val audioPlayer: AudioPlayer,
) {
    fun flow(): Flow<PlayerProgress> {
        return audioPlayer.progressFlow()
            .distinctUntilChanged()
            .map { (position, duration) ->
                val fixedPosition = if (position >= duration) 0 else position
                PlayerProgress(
                    fixedPosition,
                    duration,
                    PlayerRange()
                )
            }
    }
}
