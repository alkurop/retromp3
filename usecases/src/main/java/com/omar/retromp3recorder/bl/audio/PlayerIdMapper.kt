package com.omar.retromp3recorder.bl.audio

import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.audioplayer.observeEvents
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.rx3.asFlow
import javax.inject.Inject

class PlayerIdMapper @Inject internal constructor(
    private val audioPlayer: AudioPlayer
) {
    fun flow(): Flow<Int> {
        return audioPlayer
            .observeEvents()
            .ofType(AudioPlayer.Output.Event.AudioSessionId::class.java)
            .asFlow()
            .map { it.playerId }
    }
}
