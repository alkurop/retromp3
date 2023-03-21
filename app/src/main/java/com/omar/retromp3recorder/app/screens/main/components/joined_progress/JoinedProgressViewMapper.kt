package com.omar.retromp3recorder.app.screens.main.components.joined_progress

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.scan

object JoinedProgressViewMapper {
    fun Flow<JoinedProgressView.Output>.mapOutputToStateFlow(): Flow<JoinedProgressView.State>{
        return this.scan(JoinedProgressView.State()){oldState, output ->
            when (output) {
                is JoinedProgressView.Output.JoinedProgressChanged -> {
                    oldState.copy(
                        joinedProgress = output.joinedProgress
                    )
                }
                is JoinedProgressView.Output.CurrentFileChanged -> {
                    oldState.copy(currentFile = output.currentFile)
                }
            }
        }
    }

}
