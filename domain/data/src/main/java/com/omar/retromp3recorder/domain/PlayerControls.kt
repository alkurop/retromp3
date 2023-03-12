package com.omar.retromp3recorder.domain

data class PlayerControls(
    val loop: Loop = Loop(),
    val range: Range = Range(),
    val reverse: Reverse = Reverse(),
    val speed: Speed = Speed()
) {

    data class Range(
        val isActive: Boolean = false,
        val isVisible: Boolean = false
    )

    data class Loop(val isEnabled: Boolean = false)
    data class Reverse(val isEnabled: Boolean = false)
    data class Speed(
        val isEnabled: Boolean = false,
        val speed: Float = 1f
    )
}
