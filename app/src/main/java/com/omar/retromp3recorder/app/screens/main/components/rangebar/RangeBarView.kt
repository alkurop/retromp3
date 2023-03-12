package com.omar.retromp3recorder.app.screens.main.components.rangebar

import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.domain.FromToMillis
import com.omar.retromp3recorder.domain.PlayerRange

object RangeBarView {
    sealed class Input {
        data class RangeSet(val range: PlayerRange) : Input()
        object Enable : Input()
    }

    sealed class State {
        object Hidden : State()
        data class Visible(
            val range: PlayerRange,
            val fromToMillis: FromToMillis,
            val isActive: Boolean,
            val reset: Shell<Any>
        ) : State()
    }
}
