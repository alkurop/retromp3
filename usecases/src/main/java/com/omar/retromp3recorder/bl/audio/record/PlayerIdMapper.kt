package com.omar.retromp3recorder.bl.audio.record

import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.audioplayer.eventsFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlayerIdMapper @Inject internal constructor(
    private val audioPlayer: AudioPlayer
) {
    fun flow(): Flow<Int> {
        return audioPlayer
            .eventsFlow().filterIsInstance<AudioPlayer.Output.Event.AudioSessionId>()
            .map { it.playerId }
    }
}
