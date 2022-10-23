package com.omar.retromp3recorder.dto

enum class MenuExecutable {
    Crop,
    Open,
}

sealed interface MenuEnabler

enum class AudioEnabler : MenuEnabler {
    //todo
    Reverse,
    //todo
    Loop
}

enum class VisibilityEnabler : MenuEnabler {
    //todo
    PlaybackSpeed,
    RangeBar,
}
