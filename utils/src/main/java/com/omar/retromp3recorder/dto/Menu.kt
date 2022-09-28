package com.omar.retromp3recorder.dto

sealed class MenuAction {
    data class Popup(
        val menuExecutable: MenuExecutable,
        val isEnabled: Boolean
    ) : MenuAction()

    data class Enable(
        val enabler: MenuEnabler,
        val isEnabled: Boolean
    ) : MenuAction()
// todo   object Navigate : com.omar.retromp3recorder.dto.MenuAction()
}


enum class MenuExecutable {
    Crop,
}

sealed interface MenuEnabler

enum class AudioEnabler : MenuEnabler {
    Reverse,
    Loop
}

enum class VisibilityEnabler : MenuEnabler {
    RangeBar,

    //todo RangeBarZoom,
    PlaybackSpeed
}
