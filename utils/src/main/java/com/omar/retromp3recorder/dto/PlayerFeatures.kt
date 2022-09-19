package com.omar.retromp3recorder.dto

data class PlayerFeatures(
    val range: PlayerRange
)

data class Range(
    val isEnabled: Boolean,
    val isActive: Boolean
)

