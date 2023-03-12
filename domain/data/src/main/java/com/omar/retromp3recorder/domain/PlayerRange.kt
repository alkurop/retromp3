package com.omar.retromp3recorder.domain

data class PlayerRange(
    val from: Int = 0,
    val to: Int = 100,
    val max: Int = 100,
    val settings: PlayerControls.Range = PlayerControls.Range()
)
