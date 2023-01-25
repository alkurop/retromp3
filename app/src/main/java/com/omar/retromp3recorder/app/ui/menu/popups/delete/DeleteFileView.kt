package com.omar.retromp3recorder.app.ui.menu.popups.delete

import androidx.compose.runtime.Immutable
import com.omar.retromp3recorder.dto.ExistingFileWrapper

object DeleteFileView {
    @Immutable
    data class State(
        val shouldDismiss: Boolean = false,
        val fileWrapper: ExistingFileWrapper? = null
    )

    sealed class Input {
        object DeleteFile : Input()
    }

    sealed class Output {
        data class ShouldDismiss(val shouldDismiss: Boolean) : Output()
        data class CurrentFile(val fileWrapper: ExistingFileWrapper?) : Output()
    }
}