package com.omar.retromp3recorder.app.screens.search

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.scan

object SelectorOutputMapper {
    fun Flow<SelectorContract.Output>.mapToState(): Flow<SelectorContract.State> {
        return this.scan(SelectorContract.State()) { oldState, output ->
            when (output) {
                is SelectorContract.Output.CurrentFlow -> {
                    oldState.copy(
                        flow = output.flow
                    )
                }
                is SelectorContract.Output.CurrentFile -> {
                    val selectedFile = output.filePath
                    oldState.copy(
                        selectedFile = selectedFile
                    )
                }
            }
        }
    }
}
