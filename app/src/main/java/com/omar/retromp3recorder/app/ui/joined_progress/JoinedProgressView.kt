package com.omar.retromp3recorder.app.ui.joined_progress

import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.dto.PlayerProgress
import com.omar.retromp3recorder.dto.Wavetable

object JoinedProgressView {
    sealed class In {
        data class SeekToPosition(val position: Long) : In()
        object SeekingStarted : In()
        object SeekingFinished : In()
    }

    sealed class JoinedProgressState {
        object Hidden : JoinedProgressState()
        data class RecorderProgressShown(
            val progress: Long,
            val wavetable: Wavetable
        ) : JoinedProgressState()

        data class PlayerProgressShown(
            val progress: Shell<PlayerProgress>,
            val wavetable: Shell<Wavetable>
        ) : JoinedProgressState()
    }
}