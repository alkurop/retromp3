package com.omar.retromp3recorder.data.mock

import com.omar.retromp3recorder.domain.PlayerControls
import com.omar.retromp3recorder.domain.PlayerProgress
import com.omar.retromp3recorder.domain.PlayerRange

object MockPlayerProgressFactory {
    fun giveRange(isActive: Boolean = false, isVisible: Boolean = false): PlayerRange {
        return PlayerRange(
            from = 50,
            to = 70,
            max = 100,
            settings = PlayerControls.RangeSettings(isActive = isActive, isVisible = isVisible)
        )
    }

    fun givePlayerProgress(range: PlayerRange = giveRange()) = PlayerProgress (
        60,600, range
    )

}
