package com.omar.retromp3recorder.dto

data class PlayerFeatures(
    val range: RangeFeature
)

data class RangeFeature(
    val isEnabled: Boolean,
    val isActive: Boolean
)

