package com.omar.retromp3recorder.app.screens.home.components.speedbar

import androidx.compose.runtime.Immutable
import com.omar.retromp3recorder.domain.PlayerControls

object SpeedBarContract {
    sealed class Input {
        data class SpeedSet(val speed: Float) : Input()
        object Enable : Input()
    }

    sealed class State {
        object Hidden : State()

        @Immutable
        data class Visible(
            val speed: PlayerControls.SpeedSettings,
        ) : State()
    }
}
