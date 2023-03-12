package com.omar.retromp3recorder.app.ui.menu.popups.delete

import androidx.compose.runtime.Immutable
import com.omar.retromp3recorder.domain.ExistingFileWrapper

object DeleteFileContract {
    @Immutable
    data class State(
        val fileWrapper: ExistingFileWrapper? = null
    )

    sealed class Input {
        object DeleteFile : Input()
        object DismissPopup : Input()
    }

    sealed class Output {
        data class CurrentFile(val fileWrapper: ExistingFileWrapper?) : Output()
    }
}
