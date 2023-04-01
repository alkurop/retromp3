package com.omar.retromp3recorder.app.screens.home.components.menu.popups.delete

import androidx.compose.runtime.Immutable
import com.omar.retromp3recorder.domain.ExistingFileWrapper

object DeleteFileContract {
    @Immutable
    data class State(
        val fileWrapper: ExistingFileWrapper? = null,
        val shouldDismiss:Boolean = false
    )

    sealed class Input {
        object DeleteFile : Input()
    }

    sealed class Output {
        data class CurrentFile(val fileWrapper: ExistingFileWrapper?) : Output()
        object  Dismiss : Output()
    }
}
