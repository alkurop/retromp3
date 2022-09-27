package com.omar.retromp3recorder.app.ui.audio_controls

import com.omar.retromp3recorder.dto.FromToMillis
import com.omar.retromp3recorder.dto.PlayerProgress
import com.omar.retromp3recorder.utils.toFromToMillis

fun PlayerProgress.toProgressDisplay(): AudioControlsView.ProgressDisplay {
    val rangeSetting = this.range.settings
    val range = this.range.toFromToMillis(this.duration)
    val length = if (rangeSetting.isActive) range.length else this.duration
    val progress = if (rangeSetting.isActive) progress - range.from else progress
    return AudioControlsView.ProgressDisplay(FromToMillis(progress, length))
}
