package com.omar.retromp3recorder.dto

enum class MenuPopup {
    Crop,
    Search,
    Delete,
    Rename
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
