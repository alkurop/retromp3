package com.omar.retromp3recorder.app.screens.main.components.log

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.scan

object LogOutputMapper {

    fun Flow<LogView.Output>.mapOutputToState(): Flow<LogView.State> {
        return this.scan(LogView.State()) { oldState, output ->
            oldState.copy(
                messages = oldState.messages.takeLast(LOG_MEMORY_SIZE) + output,
            )
        }
    }
}

private const val LOG_MEMORY_SIZE = 30
