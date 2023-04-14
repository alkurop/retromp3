package com.omar.retromp3recorder.app.screens.home.components.speedbar

import androidx.compose.runtime.Immutable
import com.omar.retromp3recorder.domain.PlayerControls

object SpeedBarContract {
    sealed class Input {
        data class SpeedSet(val speed: Float) : Input()
        object Enable : Input()
    }


    sealed interface Output {
        data class Visibility(val isVisible: Boolean) : Output
        data class SpeedDataUpdate(val speed: PlayerControls.SpeedSettings) : Output
    }

    @Immutable
    data class State(
        val speedData: PlayerControls.SpeedSettings? = null,
        val isVisible: Boolean = false
    )
}
