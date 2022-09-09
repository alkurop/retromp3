package com.omar.retromp3recorder.app.ui.joined_progress

object JoinedProgressView {
    sealed class In {
        data class SeekToPosition(val position: Int) : In()
        object SeekingStarted : In()
        object SeekingFinished : In()
    }

//    sealed class JoinedProgressState {
//        object Hidden : JoinedProgressState()
//        data class RecorderProgressShown(
//            val progress: Long,
//            val wavetable: Wavetable
//        ) : JoinedProgressState()
//
//        data class PlayerProgressShown(
//            val progress: Shell<PlayerProgress>,
//            val wavetable: Shell<Wavetable>
//        ) : JoinedProgressState()
//    }
}
