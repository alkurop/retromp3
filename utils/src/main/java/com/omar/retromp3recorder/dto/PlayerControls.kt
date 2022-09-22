package com.omar.retromp3recorder.dto

data class PlayerControls(
    val range: Range = Range(),
    val loop: Loop = Loop(),
    val reverse: Reverse = Reverse()
) {

    data class Range(
        val isVisible: Boolean = false,
        val isActive: Boolean = false
    )

    data class Loop(val isEnabled: Boolean = false)
    data class Reverse(val isEnabled: Boolean = false)
}
