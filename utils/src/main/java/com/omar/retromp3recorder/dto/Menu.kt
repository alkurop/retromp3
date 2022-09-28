package com.omar.retromp3recorder.dto

enum class MenuExecutable {
    Crop,
}

sealed interface MenuEnabler

enum class AudioEnabler : MenuEnabler {
    Reverse,
    Loop
}

enum class VisibilityEnabler : MenuEnabler {
    PlaybackSpeed,
    RangeBar,
}
