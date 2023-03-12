package com.omar.retromp3recorder.app.ui.joined_progress

import androidx.compose.runtime.Immutable
import com.omar.retromp3recorder.domain.FileWrapper
import com.omar.retromp3recorder.domain.JoinedProgress

object JoinedProgressView {
    sealed class In {
        data class SeekToPosition(val position: Long) : In()
        object SeekingStarted : In()
        object SeekingFinished : In()
    }

    sealed class Output {
        data class JoinedProgressChanged(val joinedProgress: JoinedProgress) : Output()
        data class CurrentFileChanged(val currentFile: FileWrapper?) : Output()
    }

    @Immutable
    data class State(
        val joinedProgress: JoinedProgress = JoinedProgress.Hidden,
        val currentFile: FileWrapper? = null
    )
}
