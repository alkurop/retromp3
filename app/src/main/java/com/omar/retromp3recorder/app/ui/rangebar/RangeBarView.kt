package com.omar.retromp3recorder.app.ui.rangebar

import com.omar.retromp3recorder.dto.PlayerRange

object RangeBarView {
    sealed class Input {
        data class RangeSet(val range: PlayerRange) : Input()
    }

    sealed class Output {
        data class Visibility(
            val isVisible: Boolean
        ) : Output()
    }

    sealed class State {
        object Hidden : State()
        data class Visible(
            val range: PlayerRange,
            val fromTimer: Long,
            val toTimer: Long
        ) : State()
    }
}
