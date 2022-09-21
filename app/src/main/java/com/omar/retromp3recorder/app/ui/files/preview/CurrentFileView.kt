package com.omar.retromp3recorder.app.ui.files.preview

import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.dto.FileWrapper
import com.omar.retromp3recorder.dto.Wavetable

object CurrentFileView {
    data class State(
        val filePath: String? = null,
        val wavetable: Shell<Wavetable> = Shell.empty(),
        val isOpenFileActive: Boolean = false,
        val isDeleteFileActive: Boolean = false,
        val isRenameButtonActive: Boolean = false,
        val isRecording: Boolean = false,
    )

    sealed class Input {
        data class SeekToPosition(val position: Long) : Input()
        object SeekingStarted : Input()
        object SeekingFinished : Input()
    }

    sealed class Output {
        data class CurrentFileOutput(val currentFile: FileWrapper?) : CurrentFileView.Output()
        data class DeleteButtonState(val isActive: Boolean) : Output()
        data class OpenButtonState(val isActive: Boolean) : Output()
        data class RenameButtonState(val isActive: Boolean) : Output()
        data class IsRecording(val isRecording: Boolean) : Output()
    }
}