package com.omar.retromp3recorder.app.ui.log

import com.github.alkurop.stringerbell.Stringer

object LogView {
    data class State(
        val messages: List<Output>
    )

    sealed class Input
    sealed class Output {
        data class ErrorLogOutput(val error: Stringer) : LogView.Output()
        data class MessageLogOutput(val message: Stringer) : LogView.Output()
    }
}
