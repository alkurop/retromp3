package com.omar.retromp3recorder.dto

sealed class JoinedProgress {
    object Hidden : JoinedProgress()
    object Intermediate : JoinedProgress()
    data class RecorderProgressShown(
        val progress: Long,
        val wavetable: Wavetable
    ) : JoinedProgress()

    data class PlayerProgressShown(
        val progress: PlayerProgress,
        val wavetable: Wavetable?,
    ) : JoinedProgress()
}

data class PlayerProgress(
    val progress: Long,
    val duration: Long,
    val range: PlayerRange
)

data class FromToMillis(
    val from: Long,
    val to: Long
)

data class FromToMicro(
    val from: Long, val to: Long
)


