package com.omar.retromp3recorder.domain.platform

import com.github.alkurop.stringerbell.Stringer

sealed class LogEvent {
    data class Message(val message: Stringer) : LogEvent()
    data class Error(val error: Stringer) : LogEvent()
}
