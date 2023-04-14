package com.omar.retromp3recorder.app.screens.home.components.rangebar

import androidx.compose.runtime.Immutable
import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.domain.FromToMillis
import com.omar.retromp3recorder.domain.PlayerRange

object RangeBarContract {
    sealed class Input {
        data class RangeSet(val range: PlayerRange) : Input()
        object Enable : Input()
    }

    @Immutable
    data class State(
        val isVisible: Boolean = false,
        val barContent: BarContent? = null
    )

    sealed interface Output {
        data class Visibility(val isVisible: Boolean) : Output
        data class BarContentUpdate(val barContent: BarContent) : Output
    }

    data class BarContent(
        val range: PlayerRange,
        val fromToMillis: FromToMillis,
        val isActive: Boolean,
        val reset: Shell<Any>
    )
}
