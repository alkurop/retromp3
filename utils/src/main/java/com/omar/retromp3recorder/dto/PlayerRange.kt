package com.omar.retromp3recorder.dto

data class PlayerRange(
    val from: Int = 0,
    val to: Int = 100,
    val max: Int = 100,
    val settings: PlayerFeatures.Range = PlayerFeatures.Range()
)
