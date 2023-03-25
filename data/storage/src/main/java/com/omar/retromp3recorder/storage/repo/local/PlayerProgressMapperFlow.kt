package com.omar.retromp3recorder.storage.repo.local

import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.audioplayer.progressFlow
import com.omar.retromp3recorder.domain.PlayerProgress
import com.omar.retromp3recorder.domain.PlayerRange
import com.omar.retromp3recorder.utils.domain.toSeekbarTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlayerProgressMapperFlow @Inject constructor(
    private val audioPlayer: AudioPlayer,
) {
    fun execute(): Flow<PlayerProgress> {
        return audioPlayer.progressFlow()
            .distinctUntilChanged()
            .map { (position, duration) ->
                val mappedPosition = position.toSeekbarTime()
                val mappedDuration = duration.toSeekbarTime()
                val fixedPosition = if (mappedDuration == mappedPosition) 0 else position
                PlayerProgress(
                    fixedPosition,
                    duration,
                    PlayerRange()
                )
            }
    }
}
