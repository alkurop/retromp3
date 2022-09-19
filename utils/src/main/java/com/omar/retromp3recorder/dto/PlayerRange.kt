package com.omar.retromp3recorder.dto

data class PlayerRange(
    val from: Int = 0,
    val to: Int = 50,
    val max: Int = 50,
    val isEnabled: Boolean = true
)
