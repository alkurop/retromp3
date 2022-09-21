package com.omar.retromp3recorder.app.ui.rangebar

import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.dto.FromToMillis
import com.omar.retromp3recorder.dto.PlayerRange

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
