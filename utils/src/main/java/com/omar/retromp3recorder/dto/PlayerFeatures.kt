package com.omar.retromp3recorder.dto

data class PlayerFeatures(
    val range: Range = Range()
) {

    data class Range(
        val isVisible: Boolean = false,
        val isActive: Boolean = false
    )
}

