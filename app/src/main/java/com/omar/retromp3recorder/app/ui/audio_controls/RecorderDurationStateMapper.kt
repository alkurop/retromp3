package com.omar.retromp3recorder.app.ui.audio_controls

import com.omar.retromp3recorder.dto.JoinedProgress
import com.omar.retromp3recorder.storage.repo.JoinedProgressRepo
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class RecorderDurationStateMapper @Inject constructor(
    private val joinedProgressRepo: JoinedProgressRepo,
) {
    fun observe(): Observable<AudioControlsView.Output.RecorderDurationState> =
        joinedProgressRepo.observe().map { joinedProgressState ->
            when (joinedProgressState) {
                JoinedProgress.Hidden,
                is JoinedProgress.PlayerProgressShown -> AudioControlsView.Output.RecorderDurationState(null)
                is JoinedProgress.RecorderProgressShown ->AudioControlsView.Output.RecorderDurationState(joinedProgressState.progress)
            }
        }
}
