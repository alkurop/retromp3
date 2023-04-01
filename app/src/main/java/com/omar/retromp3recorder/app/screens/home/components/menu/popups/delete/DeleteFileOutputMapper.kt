package com.omar.retromp3recorder.app.screens.home.components.menu.popups.delete

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.scan

object DeleteFileOutputMapper {
    fun Flow<DeleteFileContract.Output>.mapToState():Flow<DeleteFileContract.State>{
        return this.scan(DeleteFileContract.State()){ oldState, output ->
            when (output) {
                is DeleteFileContract.Output.Dismiss -> {
                    oldState.copy(shouldDismiss = true)
                }
                is DeleteFileContract.Output.CurrentFile -> {
                    oldState.copy(fileWrapper = output.fileWrapper)
                }
            }
        }
    }
}
