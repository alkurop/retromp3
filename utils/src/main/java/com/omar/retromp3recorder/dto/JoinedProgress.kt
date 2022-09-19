package com.omar.retromp3recorder.dto

sealed class JoinedProgress {
    object Hidden : JoinedProgress()
    data class RecorderProgressShown(
        val progress: Long,
        val wavetable: Wavetable
    ) : JoinedProgress()

    data class PlayerProgressShown(
        val progress: PlayerProgress,
        val wavetable: Wavetable?,
        val range: PlayerRange = PlayerRange()
    ) : JoinedProgress()
}

data class PlayerProgress(
    val progress: Long,
    val duration: Long
)
