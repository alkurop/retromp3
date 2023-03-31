package com.omar.retromp3recorder.domain

sealed interface MenuItem

enum class MenuPopup : MenuItem {
    Crop,
    Search,
    Delete,
    Rename
}

sealed interface MenuEnabler : MenuItem

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

val MenuItem.position
    get() = order.indexOf(this)

private val order: List<MenuItem> = listOf(
    MenuPopup.Search,

    VisibilityEnabler.RangeBar,

    MenuPopup.Crop,
    MenuPopup.Delete,
    MenuPopup.Rename,

    AudioEnabler.Reverse,
    AudioEnabler.Loop,

    VisibilityEnabler.PlaybackSpeed
)


