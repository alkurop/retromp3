package com.omar.retromp3recorder.dto

sealed class MenuAction {
    data class Execute(val menuExecutable: MenuExecutable) : MenuAction()
    data class Enable(val isEnabled: Boolean, val enabler: MenuEnabler) : MenuAction()
// todo   object Navigate : com.omar.retromp3recorder.dto.MenuAction()
}

interface MenuExecutable

enum class AudioExecutable : MenuExecutable {
    Crop,
    Reverse,
    Loop
}

sealed interface MenuEnabler

enum class AudioEnabler : MenuEnabler {
    Reverse,
    Loop
}

enum class VisibilityEnabler : MenuEnabler {
    RangeBar,
    RangeBarZoom,
    PlaybackSpeed
}
