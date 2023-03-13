package com.omar.retromp3recorder.app.screens.main.components.log

import androidx.compose.runtime.Immutable
import com.github.alkurop.stringerbell.Stringer

object LogView {
    @Immutable
    data class State(
        val messages: List<Output> = emptyList()
    )

    sealed class Input
    sealed class Output {
        data class ErrorLogOutput(val error: Stringer) : Output()
        data class MessageLogOutput(val message: Stringer) : Output()
    }
}
