package com.omar.retromp3recorder.dto

data class PlayerFeatures(
    val range: RangeFeature = RangeFeature()
)

data class RangeFeature(
    val isVisible: Boolean = false,
    val isActive: Boolean = false
)

