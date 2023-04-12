package com.omar.retromp3recorder.domain

data class PlayerControls(
    val loopSettings: LoopSettings = LoopSettings(),
    val rangeSettings: RangeSettings = RangeSettings(),
    val reverseSettings: ReverseSettings = ReverseSettings(),
    val speedSettings: SpeedSettings = SpeedSettings()
) {

    data class RangeSettings(
        val isActive: Boolean = false,
        val isVisible: Boolean = false
    )

    data class LoopSettings(val isEnabled: Boolean = false)
    data class ReverseSettings(val isEnabled: Boolean = false)
    data class SpeedSettings(
        val isEnabled: Boolean = false,
        val isVisible: Boolean = false,
        val speed: Float = 1f
    )
}
