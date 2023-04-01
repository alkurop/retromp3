package com.omar.retromp3recorder.app.screens.home.components.menu.popups.rename

import com.omar.retromp3recorder.domain.ExistingFileWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.scan

object RenameFileContract {
    data class State(
        val newName: String? = null,
        val isOkButtonEnabled: Boolean = false,
        val fileWrapper: ExistingFileWrapper? = null,
        val dismiss: Boolean = false
    )

    sealed class Input {
        data class CheckCanRename(val newName: String) : Input()
        data class Rename(val newName: String) : Input()
    }

    sealed class Output {
        object Dismiss : Output()
        data class OkButtonState(val isEnabled: Boolean, val newName: String?) : Output()
        data class CurrentFile(val fileWrapper: ExistingFileWrapper) : Output()
    }
}

object RenameFileOutputMapper {
    fun Flow<RenameFileContract.Output>.mapToState(): Flow<RenameFileContract.State> {
        return this.scan(RenameFileContract.State()) { oldState, output ->
            when (output) {
                is RenameFileContract.Output.OkButtonState -> oldState.copy(
                    isOkButtonEnabled = output.isEnabled,
                    newName = output.newName
                )
                is RenameFileContract.Output.CurrentFile -> oldState.copy(fileWrapper = output.fileWrapper)
                is RenameFileContract.Output.Dismiss -> oldState.copy(dismiss = true)
            }
        }
    }
}
