package com.omar.retromp3recorder.audioplayer

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal fun Flow<AudioPlayer.Output.Progress>.mapWithOptions(
    options: () -> PlayerStartOptions
): Flow<AudioPlayer.Output.Progress> {
    return this.map {
        val range = options().rangeMillis
        if (it.end) {
            val position = if (options().isStopToRangeStartEnabled) range.from else 0
            it.copy(
                position = position, duration = options().length
            )
        } else {
            it.copy(position = it.position + range.from, duration = options().length)
        }
    }
}
